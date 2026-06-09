# Search Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Upgrade from SQL LIKE search to Elasticsearch 8.x with IK Chinese analyzer, Pinyin search, search suggestions, keyword highlighting, and aggregation filters.

**Architecture:** Spring Data Elasticsearch for ES integration, SearchService interface for future-proofing with SQL fallback. Data sync via Spring events + RabbitMQ (Canal skipped due to PG incompatibility). Frontend stays vanilla Vue 3 CDN — search.html restructured for suggest dropdown, highlight rendering, and filter counts.

**Tech Stack:** Java 21, Spring Boot 3.2.8, Spring Data Elasticsearch, Elasticsearch 8.16.0, IK Analyzer, Pinyin Analyzer, RabbitMQ 3.13, PostgreSQL 17, Vue 3 CDN

---

### Task 1: Verify Canal + PostgreSQL → Switch to Spring Events

**Decision (no code changes):**

Canal officially supports only MySQL binlog. PostgreSQL support does not exist in production-ready form. Per spec section 11: switch to **Spring Events + RabbitMQ manual sync**.

- Modify ItemController CUD methods to publish change events
- ItemSyncService consumes those events → ES

Continue to Task 2.

---

### Task 2: ES infrastructure — Dockerfile + docker-compose + Maven dependency + config

**Files:**
- Create: `infrastructure/elasticsearch/Dockerfile`
- Modify: `infrastructure/docker-compose.yml`
- Modify: `item-service/pom.xml`
- Modify: `item-service/src/main/resources/application.yml`

- [ ] **Step 1: Create ES Dockerfile**

Create `infrastructure/elasticsearch/Dockerfile`:

```dockerfile
FROM elasticsearch:8.16.0
RUN elasticsearch-plugin install --batch https://get.infini.cloud/elasticsearch/analysis-ik/8.16.0
RUN elasticsearch-plugin install --batch https://get.infini.cloud/elasticsearch/analysis-pinyin/8.16.0
```

- [ ] **Step 2: Add ES + Kibana to docker-compose**

Add to `infrastructure/docker-compose.yml` after the `minio` service, before `volumes`:

```yaml
  elasticsearch:
    build:
      context: ./elasticsearch
      dockerfile: Dockerfile
    container_name: dm-es
    environment:
      - discovery.type=single-node
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - xpack.security.enabled=false
    ports:
      - "9200:9200"
      - "9300:9300"
    volumes:
      - esdata:/usr/share/elasticsearch/data
    healthcheck:
      test: ["CMD-SHELL", "curl -f http://localhost:9200 || exit 1"]
      interval: 10s
      timeout: 5s
      retries: 10

  kibana:
    image: kibana:8.16.0
    container_name: dm-kibana
    environment:
      - ELASTICSEARCH_HOSTS=http://es:9200
    ports:
      - "5601:5601"
    depends_on:
      elasticsearch:
        condition: service_healthy
```

Add `esdata:` to the `volumes:` block at the bottom.

- [ ] **Step 3: Add ES dependency to item-service/pom.xml**

After the `spring-cloud-starter-loadbalancer` dependency block:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

- [ ] **Step 4: Add ES config to application.yml**

Append to `item-service/src/main/resources/application.yml`:

```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
    connection-timeout: 5s
    socket-timeout: 30s
```

- [ ] **Step 5: Build and verify**

```bash
cd infrastructure && docker compose build elasticsearch && docker compose up -d elasticsearch && docker compose logs elasticsearch
```

Expected: Logs show `loaded plugin [analysis-ik]` and `loaded plugin [analysis-pinyin]`.

**Commit:**
```bash
git add infrastructure/elasticsearch/Dockerfile infrastructure/docker-compose.yml item-service/pom.xml item-service/src/main/resources/application.yml
git commit -m "infra: add Elasticsearch 8.16 with IK + Pinyin plugins and Kibana"
```

---

### Task 3: ItemDocument + Repository + ES settings

**Files:**
- Create: `item-service/src/main/java/com/digital/mall/item/domain/doc/ItemDocument.java`
- Create: `item-service/src/main/resources/elasticsearch/item-index-settings.json`
- Create: `item-service/src/main/java/com/digital/mall/item/repository/ItemDocumentRepository.java`

- [ ] **Step 1: Create ItemDocument**

```java
package com.digital.mall.item.domain.doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "digital_mall_items")
@Setting(settingPath = "elasticsearch/item-index-settings.json")
public class ItemDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Integer)
    private Integer stock;

    @Field(type = FieldType.Keyword, index = false)
    private String image;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String spec;

    @Field(type = FieldType.Integer)
    private Integer sold;

    @Field(type = FieldType.Integer)
    private Integer commentCount;

    @Field(type = FieldType.Boolean)
    private Boolean isAD;

    @Field(type = FieldType.Integer)
    private Integer status;

    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
```

- [ ] **Step 2: Create ES index settings JSON**

Create `item-service/src/main/resources/elasticsearch/item-index-settings.json`:

```json
{
  "index": {
    "number_of_shards": 1,
    "number_of_replicas": 1,
    "analysis": {
      "tokenizer": {
        "pinyin": {
          "type": "pinyin",
          "keep_first_letter": true,
          "keep_separate_first_letter": false,
          "keep_full_pinyin": true,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "lowercase": true,
          "remove_duplicated_term": true
        }
      },
      "analyzer": {
        "ik_smart": { "type": "custom", "tokenizer": "ik_smart" },
        "ik_max_word": { "type": "custom", "tokenizer": "ik_max_word" },
        "pinyin": { "type": "custom", "tokenizer": "pinyin", "filter": ["lowercase"] }
      }
    }
  }
}
```

- [ ] **Step 3: Create ItemDocumentRepository**

```java
package com.digital.mall.item.repository;

import com.digital.mall.item.domain.doc.ItemDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDocumentRepository extends ElasticsearchRepository<ItemDocument, Long> {
}
```

- [ ] **Step 4: Compile check**

```bash
cd item-service && mvn compile -q
```

Expected: BUILD SUCCESS

**Commit:**
```bash
git add item-service/src/main/java/com/digital/mall/item/domain/doc/ item-service/src/main/java/com/digital/mall/item/repository/ item-service/src/main/resources/elasticsearch/
git commit -m "feat: add ItemDocument, ElasticsearchRepository, and index settings"
```

---

### Task 4: Search DTOs

**Files:**
- Create: `item-service/src/main/java/com/digital/mall/item/domain/dto/SearchSuggestionDTO.java`
- Create: `item-service/src/main/java/com/digital/mall/item/domain/dto/SearchFiltersDTO.java`

- [ ] **Step 1: Create SearchSuggestionDTO**

```java
package com.digital.mall.item.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "搜索建议")
public class SearchSuggestionDTO {

    @Schema(description = "建议文本", example = "华为 Mate 70 Pro+")
    private String text;

    @Schema(description = "高亮片段HTML", example = "<mark>华为</mark> Mate 70 Pro+")
    private String highlight;
}
```

- [ ] **Step 2: Create SearchFiltersDTO**

```java
package com.digital.mall.item.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "搜索筛选器聚合数据")
public class SearchFiltersDTO {

    @Schema(description = "分类列表")
    private List<FilterCount> categories;

    @Schema(description = "品牌列表")
    private List<FilterCount> brands;

    @Schema(description = "价格区间")
    private List<PriceRangeCount> priceRanges;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "筛选器项")
    public static class FilterCount {
        @Schema(description = "名称") private String name;
        @Schema(description = "命中数量") private Long count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "价格区间项")
    public static class PriceRangeCount {
        @Schema(description = "区间标签") private String label;
        @Schema(description = "下限(分)") private Integer min;
        @Schema(description = "上限(分)") private Integer max;
        @Schema(description = "命中数量") private Long count;
    }
}
```

- [ ] **Step 3: Compile check**

```bash
cd item-service && mvn compile -q
```

Expected: BUILD SUCCESS

**Commit:**
```bash
git add item-service/src/main/java/com/digital/mall/item/domain/dto/SearchSuggestionDTO.java item-service/src/main/java/com/digital/mall/item/domain/dto/SearchFiltersDTO.java
git commit -m "feat: add SearchSuggestionDTO and SearchFiltersDTO"
```

---

### Task 5: SearchService interface + ESSearchServiceImpl

**Files:**
- Create: `item-service/src/main/java/com/digital/mall/item/service/SearchService.java`
- Create: `item-service/src/main/java/com/digital/mall/item/service/impl/ESSearchServiceImpl.java`

- [ ] **Step 1: Create SearchService interface**

```java
package com.digital.mall.item.service;

import com.digital.mall.common.domain.PageDTO;
import com.digital.mall.item.domain.doc.ItemDocument;
import com.digital.mall.item.domain.dto.SearchFiltersDTO;
import com.digital.mall.item.domain.dto.SearchSuggestionDTO;
import com.digital.mall.item.domain.query.ItemPageQuery;

import java.util.List;

public interface SearchService {

    PageDTO<ItemDocument> search(ItemPageQuery query);

    List<SearchSuggestionDTO> suggest(String keyword);

    SearchFiltersDTO getFilters(String keyword);

    void syncAllFromDatabase();
}
```

- [ ] **Step 2: Create ESSearchServiceImpl — search + fallback**

```java
package com.digital.mall.item.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.mall.common.domain.PageDTO;
import com.digital.mall.common.utils.BeanUtils;
import com.digital.mall.item.domain.doc.ItemDocument;
import com.digital.mall.item.domain.dto.SearchFiltersDTO;
import com.digital.mall.item.domain.dto.SearchSuggestionDTO;
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
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ESSearchServiceImpl implements SearchService {

    private final ItemDocumentRepository documentRepo;
    private final ElasticsearchTemplate esTemplate;
    private final ItemMapper itemMapper;

    private static final String[] PRICE_LABELS =
        {"100以下","100-299","300-599","600-899","900-1599","1600以上"};
    private static final int[] PRICE_BOUNDS =
        {0, 10000, 30000, 60000, 90000, 160000, Integer.MAX_VALUE};

    @Override
    public PageDTO<ItemDocument> search(ItemPageQuery query) {
        try {
            return doSearch(query);
        } catch (Exception e) {
            log.error("ES search failed, falling back to SQL LIKE", e);
            return fallbackToSql(query);
        }
    }

    private PageDTO<ItemDocument> doSearch(ItemPageQuery query) {
        int page = Math.max(query.getPageNo() - 1, 0);
        int size = query.getPageSize();

        Criteria criteria = new Criteria();
        List<Criteria> shoulds = new ArrayList<>();
        List<Criteria> filters = new ArrayList<>();

        filters.add(new Criteria("status").is(1));

        if (StrUtil.isNotBlank(query.getKey())) {
            shoulds.add(new Criteria("name").matches(query.getKey()).boost(3.0f));
            shoulds.add(new Criteria("brand").matches(query.getKey()).boost(2.0f));
            shoulds.add(new Criteria("spec").matches(query.getKey()).boost(0.5f));
        }
        if (StrUtil.isNotBlank(query.getCategory())) {
            filters.add(new Criteria("category").is(query.getCategory()));
        }
        if (StrUtil.isNotBlank(query.getBrand())) {
            filters.add(new Criteria("brand").is(query.getBrand()));
        }
        if (query.getMinPrice() != null || query.getMaxPrice() != null) {
            int min = query.getMinPrice() != null ? query.getMinPrice() : 0;
            int max = query.getMaxPrice() != null ? query.getMaxPrice() : Integer.MAX_VALUE;
            filters.add(new Criteria("price").between(min, max));
        }

        Criteria root;
        if (!shoulds.isEmpty()) {
            root = new Criteria().and(new Criteria().or(shoulds.toArray(Criteria[]::new)));
        } else {
            root = new Criteria();
        }
        for (Criteria f : filters) {
            root = root.and(f);
        }

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder()
            .withQuery(new CriteriaQuery(root))
            .withPageable(PageRequest.of(page, size));

        if (StrUtil.isNotBlank(query.getKey())) {
            builder.withHighlightFields(
                new HighlightBuilder.Field("name")
                    .preTags("<mark>").postTags("</mark>"));
        }

        if (StrUtil.isNotBlank(query.getSortBy())) {
            String field = "sold".equals(query.getSortBy()) ? "sold" : "price";
            Sort sort = query.getIsAsc() != null && query.getIsAsc()
                ? Sort.by(field).ascending() : Sort.by(field).descending();
            builder.withSort(sort);
        } else {
            builder.withSort(Sort.by("updateTime").descending());
        }

        SearchHits<ItemDocument> hits = esTemplate.search(builder.build(), ItemDocument.class);
        List<ItemDocument> list = hits.getSearchHits().stream().map(hit -> {
            ItemDocument doc = hit.getContent();
            List<String> hl = hit.getHighlightField("name");
            if (hl != null && !hl.isEmpty()) doc.setName(hl.get(0));
            return doc;
        }).collect(Collectors.toList());

        PageDTO<ItemDocument> result = new PageDTO<>();
        result.setTotal(hits.getTotalHits());
        result.setPages((long) Math.ceil((double) hits.getTotalHits() / size));
        result.setList(list);
        return result;
    }

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
                .between(query.getMaxPrice() != null, Item::getPrice,
                         query.getMinPrice(), query.getMaxPrice()));
        List<ItemDocument> docs = BeanUtils.copyList(mpPage.getRecords(), ItemDocument.class);
        PageDTO<ItemDocument> result = new PageDTO<>();
        result.setTotal(mpPage.getTotal());
        result.setPages(mpPage.getPages());
        result.setList(docs);
        return result;
    }

    @Override
    public List<SearchSuggestionDTO> suggest(String keyword) {
        if (StrUtil.isBlank(keyword)) return List.of();
        try {
            Criteria c = new Criteria("name").startsWith(keyword).boost(3.0f)
                .or(new Criteria("brand").startsWith(keyword).boost(1.0f));
            NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(new CriteriaQuery(c))
                .withFilter(new CriteriaQuery(new Criteria("status").is(1)))
                .withPageable(PageRequest.of(0, 10))
                .withSort(Sort.by(Sort.Order.desc("sold")))
                .build();
            SearchHits<ItemDocument> hits = esTemplate.search(query, ItemDocument.class);
            return hits.getSearchHits().stream()
                .map(h -> SearchSuggestionDTO.builder().text(h.getContent().getName()).build())
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("ES suggest failed", e);
            return List.of();
        }
    }

    @Override
    public SearchFiltersDTO getFilters(String keyword) {
        try {
            List<SearchFiltersDTO.FilterCount> categories = queryCategoryCounts(keyword);
            List<SearchFiltersDTO.FilterCount> brands = queryBrandCounts(keyword);
            List<SearchFiltersDTO.PriceRangeCount> priceRanges = queryPriceRangeCounts(keyword);
            return SearchFiltersDTO.builder()
                .categories(categories).brands(brands).priceRanges(priceRanges).build();
        } catch (Exception e) {
            log.error("ES getFilters failed", e);
            return SearchFiltersDTO.builder()
                .categories(List.of()).brands(List.of()).priceRanges(List.of()).build();
        }
    }

    private List<SearchFiltersDTO.FilterCount> queryCategoryCounts(String keyword) {
        List<SearchFiltersDTO.FilterCount> result = new ArrayList<>();
        String[] cats = {"手机","平板","电脑"};
        for (String cat : cats) {
            long count = countWithFilter(keyword, cat, null);
            if (count > 0) result.add(new SearchFiltersDTO.FilterCount(cat, count));
        }
        return result;
    }

    private List<SearchFiltersDTO.FilterCount> queryBrandCounts(String keyword) {
        // Build list of known brands from item table
        List<String> brands = itemMapper.selectList(null).stream()
            .map(Item::getBrand).distinct().sorted().collect(Collectors.toList());
        List<SearchFiltersDTO.FilterCount> result = new ArrayList<>();
        for (String brand : brands) {
            long count = countWithFilter(keyword, null, brand);
            if (count > 0) result.add(new SearchFiltersDTO.FilterCount(brand, count));
        }
        return result;
    }

    private List<SearchFiltersDTO.PriceRangeCount> queryPriceRangeCounts(String keyword) {
        List<SearchFiltersDTO.PriceRangeCount> result = new ArrayList<>();
        for (int i = 0; i < PRICE_LABELS.length; i++) {
            int min = PRICE_BOUNDS[i];
            int max = PRICE_BOUNDS[i + 1] == Integer.MAX_VALUE
                      ? Integer.MAX_VALUE : PRICE_BOUNDS[i + 1] - 1;
            long count = countWithPriceRange(keyword, min, max);
            if (count > 0)
                result.add(new SearchFiltersDTO.PriceRangeCount(PRICE_LABELS[i], min, max, count));
        }
        return result;
    }

    private long countWithFilter(String keyword, String category, String brand) {
        Criteria c = new Criteria("status").is(1);
        if (category != null) c = c.and(new Criteria("category").is(category));
        if (brand != null) c = c.and(new Criteria("brand").is(brand));
        if (StrUtil.isNotBlank(keyword)) {
            c = c.and(new Criteria().or(
                new Criteria("name").matches(keyword),
                new Criteria("brand").matches(keyword),
                new Criteria("spec").matches(keyword)));
        }
        NativeSearchQuery q = new NativeSearchQueryBuilder()
            .withQuery(new CriteriaQuery(c)).withPageable(PageRequest.of(0, 0)).build();
        return esTemplate.search(q, ItemDocument.class).getTotalHits();
    }

    private long countWithPriceRange(String keyword, int min, int max) {
        Criteria c = new Criteria("status").is(1)
            .and(new Criteria("price").between(min, max));
        if (StrUtil.isNotBlank(keyword)) {
            c = c.and(new Criteria().or(
                new Criteria("name").matches(keyword),
                new Criteria("brand").matches(keyword),
                new Criteria("spec").matches(keyword)));
        }
        NativeSearchQuery q = new NativeSearchQueryBuilder()
            .withQuery(new CriteriaQuery(c)).withPageable(PageRequest.of(0, 0)).build();
        return esTemplate.search(q, ItemDocument.class).getTotalHits();
    }

    @Override
    public void syncAllFromDatabase() {
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
                List<ItemDocument> docs = items.stream().map(this::toDoc).collect(Collectors.toList());
                documentRepo.saveAll(docs);
                total += docs.size();
                pageNo++;
            }
        } while (!items.isEmpty());
        log.info("Full sync complete: {} items written to ES", total);
    }

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
```

- [ ] **Step 3: Compile check**

```bash
cd item-service && mvn compile -q
```

Expected: BUILD SUCCESS

**Commit:**
```bash
git add item-service/src/main/java/com/digital/mall/item/service/
git commit -m "feat: add SearchService interface and ESSearchServiceImpl with SQL fallback"
```

---

### Task 6: Rewrite SearchController

**Files:**
- Modify: `item-service/src/main/java/com/digital/mall/item/controller/SearchController.java`

- [ ] **Step 1: Replace SearchController**

```java
package com.digital.mall.item.controller;

import com.digital.mall.common.domain.PageDTO;
import com.digital.mall.common.domain.Result;
import com.digital.mall.item.domain.doc.ItemDocument;
import com.digital.mall.item.domain.dto.SearchFiltersDTO;
import com.digital.mall.item.domain.dto.SearchSuggestionDTO;
import com.digital.mall.item.domain.query.ItemPageQuery;
import com.digital.mall.item.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "搜索相关接口")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "搜索商品")
    @GetMapping("/list")
    public Result<PageDTO<ItemDocument>> search(ItemPageQuery query) {
        return Result.ok(searchService.search(query));
    }

    @Operation(summary = "搜索建议")
    @GetMapping("/suggest")
    public Result<List<SearchSuggestionDTO>> suggest(@RequestParam String keyword) {
        return Result.ok(searchService.suggest(keyword));
    }

    @Operation(summary = "获取筛选器聚合统计")
    @GetMapping("/filters")
    public Result<SearchFiltersDTO> filters(
            @RequestParam(required = false) String keyword) {
        return Result.ok(searchService.getFilters(keyword));
    }

    @Operation(summary = "全量同步PostgreSQL到ES")
    @PostMapping("/admin/sync-all")
    public Result<Map<String, Object>> syncAll() {
        searchService.syncAllFromDatabase();
        return Result.ok(Map.of("result", "ok"));
    }
}
```

- [ ] **Step 2: Compile check**

```bash
cd item-service && mvn compile -q
```

Expected: BUILD SUCCESS

**Commit:**
```bash
git add item-service/src/main/java/com/digital/mall/item/controller/SearchController.java
git commit -m "refactor: rewrite SearchController to use SearchService"
```

---

### Task 7: ItemSyncService — RabbitMQ event-driven ES sync

**Files:**
- Create: `item-service/src/main/java/com/digital/mall/item/config/MqSyncConfig.java`
- Create: `item-service/src/main/java/com/digital/mall/item/domain/dto/ItemSyncMessage.java`
- Create: `item-service/src/main/java/com/digital/mall/item/service/ItemSyncService.java`
- Modify: `item-service/src/main/java/com/digital/mall/item/controller/ItemController.java`

- [ ] **Step 1: Create MqSyncConfig**

```java
package com.digital.mall.item.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqSyncConfig {

    public static final String EXCHANGE = "digital-mall.sync";
    public static final String QUEUE = "item.sync.queue";
    public static final String ROUTING_KEY = "item.sync";

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue syncQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding syncBinding() {
        return BindingBuilder.bind(syncQueue()).to(syncExchange()).with(ROUTING_KEY);
    }
}
```

- [ ] **Step 2: Create ItemSyncMessage**

```java
package com.digital.mall.item.domain.dto;

import com.digital.mall.item.domain.po.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSyncMessage {
    /** INSERT / UPDATE / DELETE */
    private String type;
    /** changed data */
    private Item data;
    /** old data (UPDATE only) */
    private Item oldData;
}
```

- [ ] **Step 3: Create ItemSyncService**

```java
package com.digital.mall.item.service;

import com.digital.mall.item.domain.doc.ItemDocument;
import com.digital.mall.item.domain.dto.ItemSyncMessage;
import com.digital.mall.item.domain.po.Item;
import com.digital.mall.item.repository.ItemDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemSyncService {

    private final ItemDocumentRepository documentRepo;

    @RabbitListener(queues = "item.sync.queue")
    public void handleSyncMessage(ItemSyncMessage msg) {
        try {
            switch (msg.getType()) {
                case "INSERT" -> handleInsert(msg.getData());
                case "UPDATE" -> handleUpdate(msg.getData(), msg.getOldData());
                case "DELETE" -> documentRepo.deleteById(msg.getData().getId());
            }
        } catch (Exception e) {
            log.error("Failed to sync item to ES: {}", msg, e);
        }
    }

    private void handleInsert(Item item) {
        if (item != null && item.getStatus() == 1) {
            documentRepo.save(toDocument(item));
            log.debug("ES INSERT: itemId={}", item.getId());
        }
    }

    private void handleUpdate(Item newItem, Item oldItem) {
        boolean wasActive = oldItem != null && oldItem.getStatus() == 1;
        boolean nowActive = newItem != null && newItem.getStatus() == 1;
        if (wasActive && !nowActive) {
            documentRepo.deleteById(newItem.getId());
        } else if (!wasActive && nowActive) {
            documentRepo.save(toDocument(newItem));
        } else if (nowActive) {
            documentRepo.save(toDocument(newItem));
        }
    }

    private ItemDocument toDocument(Item item) {
        return ItemDocument.builder()
            .id(item.getId()).name(item.getName()).brand(item.getBrand())
            .category(item.getCategory()).price(item.getPrice()).stock(item.getStock())
            .image(item.getImage()).spec(item.getSpec()).sold(item.getSold())
            .commentCount(item.getCommentCount()).isAD(item.getIsAD())
            .status(item.getStatus()).createTime(item.getCreateTime())
            .updateTime(item.getUpdateTime()).build();
    }
}
```

- [ ] **Step 4: Modify ItemController to publish sync events**

Add import to ItemController:

```java
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.digital.mall.item.domain.dto.ItemSyncMessage;
import com.digital.mall.item.config.MqSyncConfig;
```

Replace the `private final IItemService itemService;` line with:

```java
private final IItemService itemService;
private final RabbitTemplate rabbitTemplate;
```

Add helper method at the bottom of the class:

```java
private void publishSync(String type, Item data, Item oldData) {
    ItemSyncMessage msg = ItemSyncMessage.builder()
        .type(type).data(data).oldData(oldData).build();
    rabbitTemplate.convertAndSend(MqSyncConfig.EXCHANGE,
                                   MqSyncConfig.ROUTING_KEY, msg);
}

private void publishSync(String type, Item data) {
    publishSync(type, data, null);
}
```

Modify `saveItem`:

```java
@PostMapping
public Result<Void> saveItem(@RequestBody ItemDTO item) {
    Item entity = BeanUtils.copyBean(item, Item.class);
    itemService.save(entity);
    publishSync("INSERT", entity);
    return Result.ok();
}
```

Modify `updateItem`:

```java
@PutMapping
public Result<Void> updateItem(@RequestBody ItemDTO item) {
    item.setStatus(null);
    Item newItem = BeanUtils.copyBean(item, Item.class);
    Item oldItem = itemService.getById(newItem.getId());
    itemService.updateById(newItem);
    publishSync("UPDATE", newItem, oldItem);
    return Result.ok();
}
```

Modify `updateItemStatus`:

```java
@PutMapping("/status/{id}/{status}")
public Result<Void> updateItemStatus(@PathVariable("id") Long id,
                                     @PathVariable("status") Integer status) {
    Item oldItem = itemService.getById(id);
    Item item = new Item();
    item.setId(id);
    item.setStatus(status);
    itemService.updateById(item);
    Item merged = oldItem != null ? oldItem : item;
    merged.setStatus(status);
    publishSync("UPDATE", merged, oldItem);
    return Result.ok();
}
```

Modify `deleteItemById`:

```java
@DeleteMapping("/{id}")
public Result<Void> deleteItemById(@PathVariable("id") Long id) {
    Item item = itemService.getById(id);
    itemService.removeById(id);
    if (item != null) {
        publishSync("DELETE", item);
    }
    return Result.ok();
}
```

- [ ] **Step 5: Compile check**

```bash
cd item-service && mvn compile -q
```

Expected: BUILD SUCCESS

**Commit:**
```bash
git add item-service/
git commit -m "feat: add ItemSyncService with RabbitMQ event-driven ES sync"
```

---

### Task 8: Seed data — tablet + PC products

**Files:**
- Modify: `infrastructure/sql/seed/item-phone-data.sql`

- [ ] **Step 1: Append tablet and PC data**

Add before the `-- 合计` comment at end of file:

```sql
-- ======== Apple iPad (10条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('iPad Pro 13 M4 256GB 深空黑色', 899900, 300, 'https://example.com/images/ipadpro13m4.jpg', '平板', 'Apple', '{"颜色":"深空黑色","存储":"256GB","屏幕":"13英寸","芯片":"M4"}', 45670, 8320, TRUE, 1, NOW(), NOW(), 1, 1),
('iPad Pro 13 M4 512GB 银色', 1049900, 200, 'https://example.com/images/ipadpro13m4.jpg', '平板', 'Apple', '{"颜色":"银色","存储":"512GB","屏幕":"13英寸","芯片":"M4"}', 32140, 5890, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad Pro 11 M4 256GB 深空黑色', 699900, 400, 'https://example.com/images/ipadpro11m4.jpg', '平板', 'Apple', '{"颜色":"深空黑色","存储":"256GB","屏幕":"11英寸","芯片":"M4"}', 54320, 9760, TRUE, 1, NOW(), NOW(), 1, 1),
('iPad Pro 11 M4 512GB 银色', 849900, 250, 'https://example.com/images/ipadpro11m4.jpg', '平板', 'Apple', '{"颜色":"银色","存储":"512GB","屏幕":"11英寸","芯片":"M4"}', 38650, 6980, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad Air M2 11英寸 128GB 星光色', 479900, 500, 'https://example.com/images/ipadairm2-11.jpg', '平板', 'Apple', '{"颜色":"星光色","存储":"128GB","屏幕":"11英寸","芯片":"M2"}', 67890, 12340, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad Air M2 11英寸 256GB 蓝色', 569900, 350, 'https://example.com/images/ipadairm2-11.jpg', '平板', 'Apple', '{"颜色":"蓝色","存储":"256GB","屏幕":"11英寸","芯片":"M2"}', 48760, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad Air M2 13英寸 256GB 深空灰', 659900, 280, 'https://example.com/images/ipadairm2-13.jpg', '平板', 'Apple', '{"颜色":"深空灰","存储":"256GB","屏幕":"13英寸","芯片":"M2"}', 34560, 6450, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad mini 7 128GB 粉色', 399900, 450, 'https://example.com/images/ipadmini7.jpg', '平板', 'Apple', '{"颜色":"粉色","存储":"128GB","屏幕":"8.3英寸","芯片":"A17 Pro"}', 56780, 10230, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad mini 7 256GB 紫色', 489900, 300, 'https://example.com/images/ipadmini7.jpg', '平板', 'Apple', '{"颜色":"紫色","存储":"256GB","屏幕":"8.3英寸","芯片":"A17 Pro"}', 38760, 7120, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad 第11代 256GB 银色', 349900, 600, 'https://example.com/images/ipad11.jpg', '平板', 'Apple', '{"颜色":"银色","存储":"256GB","屏幕":"10.9英寸","芯片":"A16"}', 87650, 16540, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== 华为 MatePad (10条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('华为 MatePad Pro 13.2 256GB 砚黑', 549900, 300, 'https://example.com/images/matepadpro132.jpg', '平板', '华为', '{"颜色":"砚黑","存储":"256GB","屏幕":"13.2英寸OLED","芯片":"麒麟9000s"}', 32450, 5980, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Pro 13.2 512GB 晶钻白', 649900, 180, 'https://example.com/images/matepadpro132.jpg', '平板', '华为', '{"颜色":"晶钻白","存储":"512GB","屏幕":"13.2英寸OLED","芯片":"麒麟9000s"}', 21340, 3980, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Pro 11 256GB 星河蓝', 429900, 350, 'https://example.com/images/matepadpro11.jpg', '平板', '华为', '{"颜色":"星河蓝","存储":"256GB","屏幕":"11英寸OLED","芯片":"麒麟9000s"}', 28760, 5230, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Pro 11 512GB 曜金黑', 509900, 220, 'https://example.com/images/matepadpro11.jpg', '平板', '华为', '{"颜色":"曜金黑","存储":"512GB","屏幕":"11英寸OLED","芯片":"麒麟9000s"}', 19870, 3670, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Air 12 256GB 羽砂白', 349900, 400, 'https://example.com/images/matepadair12.jpg', '平板', '华为', '{"颜色":"羽砂白","存储":"256GB","屏幕":"12英寸","芯片":"麒麟9000w"}', 38650, 7120, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Air 12 512GB 曜石灰', 429900, 280, 'https://example.com/images/matepadair12.jpg', '平板', '华为', '{"颜色":"曜石灰","存储":"512GB","屏幕":"12英寸","芯片":"麒麟9000w"}', 26780, 4980, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad 11.5 SE 128GB 海岛蓝', 199900, 500, 'https://example.com/images/matepad115se.jpg', '平板', '华为', '{"颜色":"海岛蓝","存储":"128GB","屏幕":"11.5英寸","芯片":"麒麟710A"}', 56780, 10450, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad 11.5 SE 256GB 星云灰', 249900, 400, 'https://example.com/images/matepad115se.jpg', '平板', '华为', '{"颜色":"星云灰","存储":"256GB","屏幕":"11.5英寸","芯片":"麒麟710A"}', 43210, 7890, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad SE 11 128GB 星云灰', 149900, 600, 'https://example.com/images/matepadse11.jpg', '平板', '华为', '{"颜色":"星云灰","存储":"128GB","屏幕":"11英寸","芯片":"麒麟8系列"}', 87650, 16540, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Pro 13.2 柔光版 512GB 晶钻白', 699900, 120, 'https://example.com/images/matepadpro132-paper.jpg', '平板', '华为', '{"颜色":"晶钻白","存储":"512GB","屏幕":"13.2英寸柔光屏","芯片":"麒麟9000s"}', 12340, 2340, TRUE, 1, NOW(), NOW(), 1, 1);

-- ======== 小米平板 (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('小米平板 7 Pro 256GB 黑色', 329900, 350, 'https://example.com/images/mipad7pro.jpg', '平板', '小米', '{"颜色":"黑色","存储":"256GB","屏幕":"12.4英寸","芯片":"骁龙8 Gen2"}', 38920, 7230, TRUE, 1, NOW(), NOW(), 1, 1),
('小米平板 7 Pro 512GB 远山蓝', 399900, 220, 'https://example.com/images/mipad7pro.jpg', '平板', '小米', '{"颜色":"远山蓝","存储":"512GB","屏幕":"12.4英寸","芯片":"骁龙8 Gen2"}', 27650, 5120, FALSE, 1, NOW(), NOW(), 1, 1),
('小米平板 7 256GB 深空灰', 249900, 450, 'https://example.com/images/mipad7.jpg', '平板', '小米', '{"颜色":"深空灰","存储":"256GB","屏幕":"11英寸","芯片":"骁龙7+ Gen3"}', 43210, 8120, FALSE, 1, NOW(), NOW(), 1, 1),
('小米平板 6S Pro 512GB 黑色', 299900, 300, 'https://example.com/images/mipad6spro.jpg', '平板', '小米', '{"颜色":"黑色","存储":"512GB","屏幕":"12.4英寸","芯片":"骁龙8 Gen2"}', 56780, 10450, FALSE, 1, NOW(), NOW(), 1, 1),
('小米平板 6 256GB 烟青绿', 179900, 500, 'https://example.com/images/mipad6.jpg', '平板', '小米', '{"颜色":"烟青绿","存储":"256GB","屏幕":"11英寸","芯片":"骁龙870"}', 98760, 18790, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== Samsung平板 (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('Samsung Galaxy Tab S10 Ultra 256GB 钛灰色', 799900, 150, 'https://example.com/images/tabs10ultra.jpg', '平板', 'Samsung', '{"颜色":"钛灰色","存储":"256GB","屏幕":"14.6英寸","芯片":"天玑9300+"}', 19870, 3780, TRUE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Tab S10 Ultra 512GB 钛黑色', 949900, 100, 'https://example.com/images/tabs10ultra.jpg', '平板', 'Samsung', '{"颜色":"钛黑色","存储":"512GB","屏幕":"14.6英寸","芯片":"天玑9300+"}', 13450, 2670, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Tab S10+ 256GB 雅岩灰', 599900, 200, 'https://example.com/images/tabs10plus.jpg', '平板', 'Samsung', '{"颜色":"雅岩灰","存储":"256GB","屏幕":"12.4英寸","芯片":"天玑9300+"}', 28760, 5340, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Tab S9 FE 256GB 薄荷绿', 349900, 300, 'https://example.com/images/tabs9fe.jpg', '平板', 'Samsung', '{"颜色":"薄荷绿","存储":"256GB","屏幕":"10.9英寸","芯片":"Exynos 1380"}', 45670, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Tab S9 FE+ 256GB 薰衣紫', 429900, 220, 'https://example.com/images/tabs9feplus.jpg', '平板', 'Samsung', '{"颜色":"薰衣紫","存储":"256GB","屏幕":"12.4英寸","芯片":"Exynos 1380"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== Apple MacBook (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('MacBook Pro 14 M4 512GB 深空黑色', 1499900, 200, 'https://example.com/images/mbp14m4.jpg', '电脑', 'Apple', '{"颜色":"深空黑色","存储":"512GB","屏幕":"14.2英寸","芯片":"M4","内存":"16GB"}', 21340, 4230, TRUE, 1, NOW(), NOW(), 1, 1),
('MacBook Pro 14 M4 Pro 1TB 银色', 1999900, 120, 'https://example.com/images/mbp14m4pro.jpg', '电脑', 'Apple', '{"颜色":"银色","存储":"1TB","屏幕":"14.2英寸","芯片":"M4 Pro","内存":"24GB"}', 15670, 3120, TRUE, 1, NOW(), NOW(), 1, 1),
('MacBook Pro 16 M4 Max 1TB 深空黑色', 2799900, 60, 'https://example.com/images/mbp16m4max.jpg', '电脑', 'Apple', '{"颜色":"深空黑色","存储":"1TB","屏幕":"16.2英寸","芯片":"M4 Max","内存":"36GB"}', 9870, 1980, FALSE, 1, NOW(), NOW(), 1, 1),
('MacBook Air 15 M3 512GB 午夜色', 1099900, 300, 'https://example.com/images/mba15m3.jpg', '电脑', 'Apple', '{"颜色":"午夜色","存储":"512GB","屏幕":"15.3英寸","芯片":"M3","内存":"16GB"}', 38920, 7230, FALSE, 1, NOW(), NOW(), 1, 1),
('MacBook Air 13 M3 256GB 星光色', 899900, 400, 'https://example.com/images/mba13m3.jpg', '电脑', 'Apple', '{"颜色":"星光色","存储":"256GB","屏幕":"13.6英寸","芯片":"M3","内存":"8GB"}', 54320, 10230, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== 华为 MateBook (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('华为 MateBook X Pro 2025 1TB 砚黑', 1299900, 150, 'https://example.com/images/matebookxpro2025.jpg', '电脑', '华为', '{"颜色":"砚黑","存储":"1TB","屏幕":"14.2英寸OLED","芯片":"Intel Ultra 9","内存":"32GB"}', 12340, 2450, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 MateBook X Pro 2025 512GB 皓月银', 1099900, 200, 'https://example.com/images/matebookxpro2025.jpg', '电脑', '华为', '{"颜色":"皓月银","存储":"512GB","屏幕":"14.2英寸OLED","芯片":"Intel Ultra 7","内存":"16GB"}', 9870, 1890, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MateBook 16s 512GB 深空灰', 799900, 250, 'https://example.com/images/matebook16s.jpg', '电脑', '华为', '{"颜色":"深空灰","存储":"512GB","屏幕":"16英寸","芯片":"Intel Ultra 7","内存":"16GB"}', 16780, 3120, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MateBook D 16 512GB 深空灰', 549900, 400, 'https://example.com/images/matebookd16.jpg', '电脑', '华为', '{"颜色":"深空灰","存储":"512GB","屏幕":"16英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 28760, 5340, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MateBook 14 512GB 皓月银', 629900, 350, 'https://example.com/images/matebook14.jpg', '电脑', '华为', '{"颜色":"皓月银","存储":"512GB","屏幕":"14英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 34560, 6450, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== 联想电脑 (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('ThinkPad X1 Carbon Gen 13 512GB 黑色', 1199900, 150, 'https://example.com/images/x1cgen13.jpg', '电脑', '联想', '{"颜色":"黑色","存储":"512GB","屏幕":"14英寸","芯片":"Intel Ultra 7","内存":"32GB"}', 9870, 1980, TRUE, 1, NOW(), NOW(), 1, 1),
('ThinkPad X1 Carbon Gen 13 1TB 黑色', 1499900, 80, 'https://example.com/images/x1cgen13.jpg', '电脑', '联想', '{"颜色":"黑色","存储":"1TB","屏幕":"14英寸","芯片":"Intel Ultra 7","内存":"32GB"}', 6540, 1340, FALSE, 1, NOW(), NOW(), 1, 1),
('联想 Yoga Pro 14s 512GB 深空灰', 799900, 200, 'https://example.com/images/yogapro14s.jpg', '电脑', '联想', '{"颜色":"深空灰","存储":"512GB","屏幕":"14.5英寸","芯片":"Intel Ultra 7","内存":"32GB"}', 16780, 3120, FALSE, 1, NOW(), NOW(), 1, 1),
('联想 小新 Pro 16 512GB 鸽子灰', 649900, 350, 'https://example.com/images/xiaoxinpro16.jpg', '电脑', '联想', '{"颜色":"鸽子灰","存储":"512GB","屏幕":"16英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1),
('联想 小新 14 512GB 霜雪银', 499900, 500, 'https://example.com/images/xiaoxin14.jpg', '电脑', '联想', '{"颜色":"霜雪银","存储":"512GB","屏幕":"14英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 56780, 10450, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== Dell电脑 (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('Dell XPS 14 512GB 铂金银', 1199900, 120, 'https://example.com/images/xps14.jpg', '电脑', 'Dell', '{"颜色":"铂金银","存储":"512GB","屏幕":"14.5英寸OLED","芯片":"Intel Ultra 7","内存":"16GB"}', 8790, 1760, TRUE, 1, NOW(), NOW(), 1, 1),
('Dell XPS 14 1TB 石墨黑', 1499900, 80, 'https://example.com/images/xps14.jpg', '电脑', 'Dell', '{"颜色":"石墨黑","存储":"1TB","屏幕":"14.5英寸OLED","芯片":"Intel Ultra 9","内存":"32GB"}', 5670, 1120, FALSE, 1, NOW(), NOW(), 1, 1),
('Dell XPS 16 1TB 铂金银', 1799900, 50, 'https://example.com/images/xps16.jpg', '电脑', 'Dell', '{"颜色":"铂金银","存储":"1TB","屏幕":"16.3英寸OLED","芯片":"Intel Ultra 9","内存":"32GB"}', 4320, 890, FALSE, 1, NOW(), NOW(), 1, 1),
('Dell Inspiron 16 Plus 512GB 冰河蓝', 699900, 250, 'https://example.com/images/inspiron16plus.jpg', '电脑', 'Dell', '{"颜色":"冰河蓝","存储":"512GB","屏幕":"16英寸","芯片":"Intel Ultra 7","内存":"16GB"}', 21340, 4230, FALSE, 1, NOW(), NOW(), 1, 1),
('Dell Inspiron 14 512GB 铂金银', 549900, 350, 'https://example.com/images/inspiron14.jpg', '电脑', 'Dell', '{"颜色":"铂金银","存储":"512GB","屏幕":"14英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1);

-- total: phones ~155 + tablets 30 + PCs 20 ≈ 205 items
```

- [ ] **Step 2: Verify SQL syntax**

Manual review — all commas correct, all values quoted properly.

**Commit:**
```bash
git add infrastructure/sql/seed/item-phone-data.sql
git commit -m "data: add tablet (30) and PC (20) seed products"
```

---

### Task 9: Frontend — search.html + index.html

**Files:**
- Modify: `nginx-1.26.2/html/digital-mall-portal/search.html`
- Modify: `nginx-1.26.2/html/digital-mall-portal/index.html`

- [ ] **Step 1: Fix index.html search redirect**

In `index.html`, find the `search` method. Change:
```javascript
window.location.href = '/search.html?keyword=' + encodeURIComponent(kw);
```
to:
```javascript
window.location.href = '/search.html?key=' + encodeURIComponent(kw);
```

- [ ] **Step 2: Rewrite search.html — data() section**

Replace the `data()` block (approximately lines 381-418) with:

```javascript
data() {
    return {
        keyword: '',
        items: [],
        total: 0, currentPage: 1, pageSize: 20, totalPages: 0, loading: false,
        sortBy: '', isAsc: true,
        selectedCategory: '', selectedBrand: '', priceMin: null, priceMax: null,
        categories: [{ name: '全部', count: 0 }],
        brands: [{ name: '全部', count: 0 }],
        suggestions: [], showSuggest: false, suggestTimer: null,
        priceRanges: [
            { label: '100以下', min: 0, max: 10000, count: 0 },
            { label: '100-299',   min: 10000, max: 29900, count: 0 },
            { label: '300-599',   min: 30000, max: 59900, count: 0 },
            { label: '600-899',   min: 60000, max: 89900, count: 0 },
            { label: '900-1599',  min: 90000, max: 159900, count: 0 },
            { label: '1600以上',  min: 160000, max: null, count: 0 }
        ],
        util
    };
},
```

- [ ] **Step 3: Replace methods section**

Replace the entire `methods:` block (lines 448-636) with:

```javascript
methods: {
    onKeywordInput() {
        if (this.suggestTimer) clearTimeout(this.suggestTimer);
        const kw = this.keyword.trim();
        if (!kw) { this.suggestions = []; this.showSuggest = false; return; }
        this.suggestTimer = setTimeout(() => {
            axios.get('/search/suggest', { params: { keyword: kw } })
                .then(resp => {
                    const data = resp.data || resp;
                    if (Array.isArray(data)) {
                        this.suggestions = data.slice(0, 10);
                        this.showSuggest = this.suggestions.length > 0;
                    }
                }).catch(() => { this.suggestions = []; });
        }, 300);
    },
    selectSuggestion(s) {
        this.keyword = s.text;
        this.showSuggest = false;
        this.handleSearch();
    },
    hideSuggest() { setTimeout(() => { this.showSuggest = false; }, 200); },
    fetchFilters() {
        const kw = this.keyword && this.keyword.trim() || '';
        axios.get('/search/filters', { params: { keyword: kw } })
            .then(resp => {
                const d = resp.data || resp;
                if (d && d.categories) {
                    let total = d.categories.reduce((s,c) => s + c.count, 0);
                    this.categories = [{ name: '全部', count: total }];
                    d.categories.forEach(c => this.categories.push(c));
                }
                if (d && d.brands) this.brands = d.brands;
                if (d && d.priceRanges) {
                    d.priceRanges.forEach(pr => {
                        const m = this.priceRanges.find(p => p.label === pr.label);
                        if (m) m.count = pr.count;
                    });
                }
            }).catch(() => {
                this.categories = [{ name: '全部', count: 0 },
                    { name: '手机', count: 0 }, { name: '平板', count: 0 }, { name: '电脑', count: 0 }];
                this.brands = [{ name: '全部', count: 0 }];
            });
    },
    handleSearch() { this.currentPage = 1; this.doSearch(); },
    doSearch() {
        this.loading = true; this.items = [];
        const params = { pageNo: this.currentPage, pageSize: this.pageSize };
        if (this.keyword && this.keyword.trim()) params.key = this.keyword.trim();
        if (this.sortBy) { params.sortBy = this.sortBy; params.isAsc = this.isAsc; }
        if (this.selectedCategory) params.category = this.selectedCategory;
        if (this.selectedBrand) params.brand = this.selectedBrand;
        if (this.priceMin !== null) params.minPrice = this.priceMin;
        if (this.priceMax !== null) params.maxPrice = this.priceMax;

        axios.get('/search/list', { params })
            .then(resp => {
                const d = resp.data || resp;
                if (d.list) { this.items = d.list; this.total = d.total || 0; this.totalPages = d.pages || 1; }
                else if (d.records) { this.items = d.records; this.total = d.total || 0; this.totalPages = d.pages || 1; }
                else if (Array.isArray(d)) { this.items = d; this.total = d.length; this.totalPages = 1; }
                else { this.items = []; this.total = 0; this.totalPages = 0; }
                this.loading = false;
                this.fetchFilters();
            }).catch(err => { console.error(err); this.items = []; this.total = 0; this.totalPages = 0; this.loading = false; });
    },
    selectCategory(cat) { this.selectedCategory = (cat === '全部') ? '' : cat; this.currentPage = 1; this.doSearch(); },
    selectBrand(brand) { this.selectedBrand = (brand === '全部') ? '' : brand; this.currentPage = 1; this.doSearch(); },
    selectPrice(min, max) {
        if (this.priceMin === min && this.priceMax === max) { this.priceMin = null; this.priceMax = null; }
        else { this.priceMin = min; this.priceMax = max; }
        this.currentPage = 1; this.doSearch();
    },
    clearAll() {
        this.keyword = ''; this.selectedCategory = ''; this.selectedBrand = '';
        this.priceMin = null; this.priceMax = null; this.sortBy = ''; this.isAsc = true; this.currentPage = 1;
        if (window.history) window.history.replaceState({}, '', window.location.pathname);
        this.doSearch();
    },
    changeSort(field) {
        if (this.sortBy === field) this.isAsc = !this.isAsc;
        else { this.sortBy = field; this.isAsc = true; }
        this.currentPage = 1; this.doSearch();
    },
    changePage(page) {
        if (page < 1 || page > this.totalPages || page === this.currentPage) return;
        this.currentPage = page; this.doSearch();
        window.scrollTo({ top: 280, behavior: 'smooth' });
    },
    goDetail(id) { location.href = '/detail.html?id=' + id; },
    addCart(item) {
        if (!util.isLogin()) { location.href = '/login.html'; return; }
        axios.post('/carts', {
            itemId: item.id, name: item.name.replace(/<[^>]+>/g, ''),
            spec: item.spec || '', price: item.price, image: item.image || ''
        }).then(() => { window.dispatchEvent(new CustomEvent('cart-updated')); })
          .catch(err => { console.error('Add to cart failed:', err); });
    }
},
```

- [ ] **Step 4: Add suggest-box to search hero template**

After the `</button>` in `.search-bar`, add:

```html
<div class="suggest-box" v-if="showSuggest && suggestions.length > 0">
    <div class="suggest-item" v-for="(s, idx) in suggestions" :key="idx"
         @mousedown.prevent="selectSuggestion(s)">
      🔍 <span v-html="s.highlight || s.text"></span>
    </div>
</div>
```

- [ ] **Step 5: Add suggest CSS to <style> block**

Add before `/* responsive */`:

```css
.suggest-box {
    position: absolute; top: 100%; left: 0; right: 0;
    background: #fff; border: 1px solid var(--border);
    border-radius: 0 0 12px 12px;
    box-shadow: 0 8px 24px rgba(0,0,0,.1); z-index: 100;
    text-align: left; overflow: hidden;
}
.suggest-item {
    padding: 12px 20px; font: 500 14px 'Nunito Sans', sans-serif;
    cursor: pointer; transition: background .15s;
}
.suggest-item:hover { background: var(--primary-light); }
.suggest-item mark { background: none; color: var(--primary); font-weight: 700; }
```

And add to `.search-hero .search-bar`: `position: relative;`

- [ ] **Step 6: Update search input attributes**

Change:
```html
<input ... @keyup.enter="handleSearch" ...>
```
to:
```html
<input ... @keyup.enter="handleSearch" @input="onKeywordInput" @blur="hideSuggest" ...>
```

- [ ] **Step 7: Update product card name for highlight**

Change `{{ item.name }}` to `<span v-html="item.name"></span>`

- [ ] **Step 8: Update filter tags to show counts**

Category filter：`{{ cat.name }} ({{ cat.count }})`
Brand filter：`{{ b.name }} ({{ b.count }})`

- [ ] **Step 9: Update mounted hook**

```javascript
mounted() {
    const key = util.getUrlParam('key');
    if (key) { this.keyword = decodeURIComponent(key); }
    this.fetchFilters();
    this.doSearch();
}
```

- [ ] **Step 10: Keep existing `computed` properties and `displayPages` / `showRightEllipsis` unchanged**

These remain as-is in the file — only `data`, `methods`, template sections above change.

**Commit:**
```bash
git add nginx-1.26.2/html/digital-mall-portal/search.html nginx-1.26.2/html/digital-mall-portal/index.html
git commit -m "feat: restructure search frontend for ES API + suggest + highlight + filter counts"
```

---

### Task 10: End-to-end verification

- [ ] **Step 1: Start all infrastructure**

```bash
cd infrastructure && docker compose up -d
```

Expected: postgres, nacos, redis, rabbitmq, elasticsearch, kibana all healthy.

- [ ] **Step 2: Start item-service**

```bash
cd item-service && mvn spring-boot:run
```

- [ ] **Step 3: Full sync seed data to ES**

```bash
curl -X POST http://localhost:18083/api/search/admin/sync-all
```

Expected: `{"code":200,"msg":"OK","data":{"result":"ok"}}`

- [ ] **Step 4: Verify ES index**

Kibana Dev Tools at `http://localhost:5601`:
```
GET /digital_mall_items/_count
```
Expected: `{"count": ~205}`

- [ ] **Step 5: Test search API**

```bash
curl "http://localhost:18083/api/search/list?key=华为&pageNo=1&pageSize=10"
```

Expected: `"total"` > 0, items returned.

- [ ] **Step 6: Test suggest API**

```bash
curl "http://localhost:18083/api/search/suggest?keyword=hua"
```

Expected: suggestion list returned.

- [ ] **Step 7: Test filters API**

```bash
curl "http://localhost:18083/api/search/filters?keyword=华为"
```

Expected: categories/brands/priceRanges with counts.

- [ ] **Step 8: Test CRUD sync**

```bash
curl -X POST http://localhost:18083/api/items -H "Content-Type: application/json" \
  -d '{"name":"test ES sync item","price":99900,"stock":10,"category":"手机","brand":"测试"}'

# verify in ES via Kibana or search API
curl "http://localhost:18083/api/search/list?key=test"

# delete
curl -X DELETE http://localhost:18083/api/items/<id>
```

Expected: Item syncs to/from ES after CUD operations.

- [ ] **Step 9: Frontend smoke test**

- Open `http://localhost:18083/search.html`
- Type "huawei" → see suggestions
- Search → see results with highlights
- Click category/brand/price filters → verify filtering works
- Verify pagination, sort by price/sales

No commit — verification only.

---

## Self-Review

**Spec coverage:** Every spec section mapped to tasks above: infrastructure (Task 2), index/document (Task 3), Java layer (Tasks 4-6), sync (Task 7), seed data (Task 8), frontend (Task 9), verification (Task 10). ✅

**Placeholders:** None. All code shown is complete. ✅

**Type consistency:** `ItemPageQuery.key` ↔ `params.key` ↔ `query.getKey()`. `PageDTO<ItemDocument>` ↔ `data.list`. `SearchFiltersDTO.FilterCount` ↔ `{ name, count }`. Sort field `sold` consistent across all tasks. ✅
