package com.digital.mall.item.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.mall.common.domain.PageDTO;
import com.digital.mall.common.utils.BeanUtils;
import com.digital.mall.item.domain.doc.ItemDocument;
import com.digital.mall.item.domain.po.Item;
import com.digital.mall.item.domain.query.ItemPageQuery;
import com.digital.mall.item.mapper.ItemMapper;
import com.digital.mall.item.repository.ItemDocumentRepository;
import com.digital.mall.item.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 搜索服务实现（Elasticsearch 主引擎 + MySQL 降级兜底）
 * <p>
 * 使用 Spring Data Elasticsearch + Criteria API 构建动态查询条件，
 * 基于 IK 中文分词实现商品全文检索。ES 查询失败时自动降级为
 * MyBatis-Plus 的 SQL LIKE 模糊查询，保证搜索功能高可用。
 * <p>
 * 核心设计：
 * <ul>
 *   <li>多字段加权匹配：name(3.0) &gt; brand(2.0) &gt; spec(0.5)</li>
 *   <li>写搜分离分析器：写入用 ik_smart（粗粒度），搜索用 ik_max_word（细粒度），提高召回率</li>
 *   <li>关键词高亮：用 &lt;mark&gt; 标签包裹命中文本</li>
 *   <li>默认排序：按更新时间倒序</li>
 * </ul>
 *
 * @author digital-mall
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ESSearchServiceImpl implements SearchService {

    /** ES 文档 Repository，提供索引层的 CRUD 操作 */
    private final ItemDocumentRepository documentRepo;
    /** Elasticsearch 模板，用于构建和执行动态查询 */
    private final ElasticsearchTemplate esTemplate;
    /** 商品 Mapper，用于 ES 降级时的 MySQL 查询和全量同步 */
    private final ItemMapper itemMapper;

    // ==================== search ====================

    /**
     * 多条件组合搜索商品（主入口）
     * <p>
     * 优先使用 ES 全文检索，捕获任何异常后自动降级为 MySQL LIKE 模糊查询，
     * 确保搜索功能不会因 ES 不可用而完全中断。
     *
     * @param query 搜索条件（关键词、分类、品牌、价格区间、排序、分页）
     * @return 商品分页结果，每条结果的名称中关键词被 &lt;mark&gt; 标签高亮
     */
    @Override
    public PageDTO<ItemDocument> search(ItemPageQuery query) {
        try {
            return doSearch(query);
        } catch (Exception e) {
            log.error("ES search failed, falling back to SQL LIKE", e);
            return fallbackToSql(query);
        }
    }

    /**
     * 执行 ES 搜索
     * <p>
     * 分页参数从 PageQuery 的 1-based 转为 ES 的 0-based；
     * 构建 Criteria 动态查询条件 → 执行搜索 → 对结果名称做关键词高亮。
     */
    private PageDTO<ItemDocument> doSearch(ItemPageQuery query) {
        int page = Math.max(query.getPageNo() - 1, 0);
        int size = query.getPageSize();

        Criteria criteria = buildSearchCriteria(query);

        // 构建查询条件
        Query q = CriteriaQuery.builder(criteria)
            .withPageable(PageRequest.of(page, size))
            .withSort(buildSort(query))
            .build();

        // 执行搜索
        SearchHits<ItemDocument> hits = esTemplate.search(q, ItemDocument.class);

        // 对结果名称做关键词高亮
        String key = StrUtil.isNotBlank(query.getKey()) ? query.getKey() : null;
        List<ItemDocument> list = hits.getSearchHits().stream().map(hit -> {
            ItemDocument doc = hit.getContent();
            if (key != null && doc.getName() != null) {
                doc.setName(highlightKeyword(doc.getName(), key));
            }
            return doc;
        }).collect(Collectors.toList());

        PageDTO<ItemDocument> result = new PageDTO<>();
        result.setTotal(hits.getTotalHits());
        result.setPages((long) Math.ceil((double) hits.getTotalHits() / size));
        result.setList(list);
        return result;
    }

    /**
     * 动态构建 ES 搜索条件
     * <p>
     * 按顺序叠加过滤条件：
     * <ol>
     *   <li>基础过滤：仅搜索上架商品（status=1）</li>
     *   <li>分类 / 品牌：Keyword 精确匹配</li>
     *   <li>价格区间：between 范围查询</li>
     *   <li>关键词：多字段加权匹配 — name(3.0) + brand(2.0) + spec(0.5)</li>
     * </ol>
     *
     * @param query 用户输入的搜索条件
     * @return 组合后的 ES Criteria 查询条件
     */
    private Criteria buildSearchCriteria(ItemPageQuery query) {
        // 基础过滤：仅搜索上架商品
        Criteria criteria = new Criteria("status").is(1);

        // 分类精确匹配（Keyword 类型）
        if (StrUtil.isNotBlank(query.getCategory())) {
            criteria = criteria.and("category").is(query.getCategory());
        }
        // 品牌精确匹配（Keyword 类型）
        if (StrUtil.isNotBlank(query.getBrand())) {
            criteria = criteria.and("brand").is(query.getBrand());
        }
        // 价格区间查询（闭区间）
        if (query.getMinPrice() != null || query.getMaxPrice() != null) {
            int min = query.getMinPrice() != null ? query.getMinPrice() : 0;
            int max = query.getMaxPrice() != null ? query.getMaxPrice() : Integer.MAX_VALUE;
            criteria = criteria.and("price").between(min, max);
        }

        // 关键词多字段加权匹配
        // name 权重最高(3.0)：名称匹配是搜索的核心
        // brand 次之(2.0)：允许品牌名命中
        // spec 最低(0.5)：规格参数仅辅助匹配，不主导结果排序
        // key为搜索框的输入
        if (StrUtil.isNotBlank(query.getKey())) {
            String key = query.getKey();
            Criteria keywordCriteria = new Criteria("name").matches(key).boost(3.0f)
                .or("brand").matches(key).boost(2.0f)
                .or("spec").matches(key).boost(0.5f);
            criteria = criteria.and(keywordCriteria);
        }

        return criteria;
    }

    /**
     * 构建排序规则
     * <p>
     * 用户可选择按价格（price）或销量（sold）升降序排列；
     * 未指定排序字段时，默认按更新时间（updateTime）降序。
     *
     * @param query 搜索条件，包含 sortBy 和 isAsc
     * @return ES Sort 对象
     */
    private Sort buildSort(ItemPageQuery query) {
        if (StrUtil.isNotBlank(query.getSortBy())) {
            // 仅支持按销量(sold)或价格(price)排序
            String field = "sold".equals(query.getSortBy()) ? "sold" : "price";
            Sort sort = query.getIsAsc() != null && query.getIsAsc()
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();
            return sort;
        }
        // 默认排序：最新更新的商品在前
        return Sort.by("updateTime").descending();
    }

    // ==================== fallback to SQL ====================

    /**
     * ES 搜索降级 → MySQL LIKE 模糊查询
     * <p>
     * 当 ES 不可用时，使用 MyBatis-Plus 的 LambdaQueryWrapper 构建等价的
     * SQL 查询条件。关键词搜索使用 LIKE '%keyword%' 模糊匹配商品名和品牌。
     * <p>
     * 注意：SQL 降级不支持 IK 分词，也无法对 spec 字段做关键词匹配，
     * 但能保证核心搜索功能可用。
     *
     * @param query 搜索条件（与 ES 查询使用相同的参数对象）
     * @return 分页结果（无关键词高亮）
     */
    private PageDTO<ItemDocument> fallbackToSql(ItemPageQuery query) {
        Page<Item> mpPage = itemMapper.selectPage(
            query.toMpPage("update_time", false),
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Item>()
                .and(StrUtil.isNotBlank(query.getKey()), w ->
                    w.like(Item::getName, query.getKey())
                     .or().like(Item::getBrand, query.getKey()))
                .eq(StrUtil.isNotBlank(query.getBrand()), Item::getBrand, query.getBrand())
                .eq(StrUtil.isNotBlank(query.getCategory()), Item::getCategory, query.getCategory())
                .eq(Item::getStatus, 1)
                .between(query.getMinPrice() != null || query.getMaxPrice() != null, Item::getPrice,
                         query.getMinPrice() != null ? query.getMinPrice() : 0,
                         query.getMaxPrice() != null ? query.getMaxPrice() : Integer.MAX_VALUE));
        List<ItemDocument> docs = BeanUtils.copyList(mpPage.getRecords(), ItemDocument.class);
        PageDTO<ItemDocument> result = new PageDTO<>();
        result.setTotal(mpPage.getTotal());
        result.setPages(mpPage.getPages());
        result.setList(docs);
        return result;
    }

    // ==================== sync ====================

    /**
     * 全量同步数据库商品到 ES 索引
     * <p>
     * 先删除旧索引（确保 mapping/settings 如 IK 分词器、lowercase 过滤器
     * 的最新配置生效），再分页（每页500条）读取所有上架商品并批量写入 ES。
     * <p>
     * 这是一个管理运维接口，适用于：
     * <ul>
     *   <li>首次部署时建立 ES 索引</li>
     *   <li>索引 mapping/settings 变更后重建</li>
     *   <li>ES 数据丢失后的全量恢复</li>
     * </ul>
     */
    @Override
    public void syncAllFromDatabase() {
        // 删除并重建索引，确保最新的 mapping/settings 生效（如 lowercase 过滤器）
        try {
            esTemplate.indexOps(ItemDocument.class).delete();
            log.info("Deleted existing ES index, will recreate with latest settings");
        } catch (Exception e) {
            log.info("No existing ES index to delete, will create fresh");
        }

        int pageSize = 500, pageNo = 1;
        long total = 0;
        List<Item> items;
        do {
            Page<Item> page = new Page<>(pageNo, pageSize);
            Page<Item> result = itemMapper.selectPage(page,
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Item>()
                    .eq(Item::getStatus, 1));
            items = result.getRecords();
            if (!items.isEmpty()) {
                documentRepo.saveAll(items.stream().map(this::toDoc)
                    .collect(Collectors.toList()));
                total += items.size();
                pageNo++;
            }
        } while (!items.isEmpty());
        log.info("Full sync complete: {} items written to ES", total);
    }

    // ==================== util ====================

    /**
     * 关键词高亮处理
     * <p>
     * 使用正则（大小写不敏感）将文本中匹配的关键词用 &lt;mark&gt; 标签包裹。
     * 使用 Pattern.quote 转义关键词中的正则特殊字符，防止注入。
     *
     * @param text    原始文本
     * @param keyword 需要高亮的关键词
     * @return 高亮后的 HTML 文本，null 入参或正则异常时返回原始文本
     */
    private static String highlightKeyword(String text, String keyword) {
        if (text == null || keyword == null) return text;
        try {
            return text.replaceAll("(?i)(" + Pattern.quote(keyword) + ")",
                                   "<mark>$1</mark>");
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * 将数据库实体 Item 转换为 ES 文档 ItemDocument
     * <p>
     * 一对一字段映射，用于全量同步和增量同步。
     *
     * @param item 数据库商品实体
     * @return ES 文档对象
     */
    private ItemDocument toDoc(Item item) {
        return ItemDocument.builder()
            .id(item.getId()).name(item.getName()).brand(item.getBrand())
            .category(item.getCategory()).price(item.getPrice()).stock(item.getStock())
            .image(item.getImage()).spec(item.getSpec()).sold(item.getSold())
            .commentCount(item.getCommentCount()).isAD(item.getIsAD())
            .status(item.getStatus()).createTime(item.getCreateTime())
            .updateTime(item.getUpdateTime()).build();
    }
}
