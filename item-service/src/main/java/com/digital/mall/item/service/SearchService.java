package com.digital.mall.item.service;

import com.digital.mall.common.domain.PageDTO;
import com.digital.mall.item.domain.doc.ItemDocument;
import com.digital.mall.item.domain.query.ItemPageQuery;

/**
 * 搜索服务接口
 * <p>
 * 定义商品全文检索的核心能力：多条件搜索、数据同步。
 *
 * @author digital-mall
 */
public interface SearchService {

    /**
     * 多条件组合搜索商品
     * <p>
     * 支持关键词（多字段加权匹配）、分类、品牌、价格区间等条件组合，
     * 返回分页结果。ES 不可用时自动降级为 MySQL LIKE 查询。
     *
     * @param query 搜索条件（关键词、分类、品牌、价格区间、排序、分页）
     * @return 商品分页结果
     */
    PageDTO<ItemDocument> search(ItemPageQuery query);

    /**
     * 全量同步数据库商品到 ES 索引
     * <p>
     * 删除旧索引后分页读取所有上架商品并批量写入ES，
     * 确保最新的 mapping/settings 生效。
     */
    void syncAllFromDatabase();
}
