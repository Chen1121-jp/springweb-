# 数字商城搜索系统设计文档

> 创建日期: 2026-06-03
> 状态: 待审核
> 目标: Level 2 中等升级 (Elasticsearch 分词搜索 + 拼音 + 搜索建议 + 相关性排序)，为 Level 3 完整搜索引擎打好基础

---

## 1. 目标与范围

### 1.1 核心目标

将现有 SQL LIKE 搜索升级为基于 Elasticsearch 8.x 的专业搜索引擎：

- 中文分词搜索 (IK Analyzer)
- 拼音搜索 (Pinyin Analyzer)
- 搜索建议/自动补全
- 关键词高亮
- 聚合筛选统计 (分类/品牌/价格区间命中数)
- 价格区间 filter + 销量/价格/时间排序
- Canal binlog 异步同步，零侵入现有业务代码

### 1.2 分期

| 阶段 | 内容 | 本次 |
|------|------|:---:|
| Level 1 | SQL LIKE 跑通 | (跳过) |
| Level 2 | ES + IK + Pinyin + suggest + 高亮 + 聚合 | ✅ |
| Level 3 | 搜索日志 + 热词 + 同义词 + 纠错 + 个性化 | 后续 |

### 1.3 分类

数字商城固定三个分类：

| 分类 | 说明 |
|------|------|
| 手机 | 智能手机、折叠屏 |
| 平板 | 平板电脑 |
| 电脑 | 笔记本、台式机 |

品牌由 ES 聚合动态获取，前端展示命中统计数。

---

## 2. 整体架构

```
用户浏览器 → Nginx :18083 → Gateway :8080 → item-service
                                                 │
                        ┌────────────────────────┼────────────────────────┐
                        │                        │                        │
                 SearchController          ItemController            其他路由
                 (/search/**)              (/items/**)
                        │
                  SearchService (接口)
                        │
              ESSearchServiceImpl ──── Elasticsearch 8.x
                        │                    ▲
              ItemSyncService ───────────────┘
                        │                    │
                  RabbitMQ              Canal Server
                        │                    │
                  PostgreSQL ◄───────────────┘ (binlog)
```

**写路径**: PG CRUD (不改) → Canal binlog → RabbitMQ → ItemSyncService → ES
**读路径**: 用户 → Nginx → Gateway → SearchController → ES
**降级**: ES 不可用时 SearchService 自动切换 SQL LIKE

---

## 3. 基础设施

### 3.1 docker-compose 新增

```yaml
elasticsearch:
  build:
    context: ./elasticsearch
    dockerfile: Dockerfile
  container_name: es
  environment:
    - discovery.type=single-node
    - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    - xpack.security.enabled=false
  ports:
    - "9200:9200"
    - "9300:9300"
  volumes:
    - es-data:/usr/share/elasticsearch/data

kibana:
  image: kibana:8.16.0
  container_name: kibana
  environment:
    - ELASTICSEARCH_HOSTS=http://es:9200
  ports:
    - "5601:5601"
  depends_on:
    elasticsearch:
      condition: service_healthy

canal-server:
  image: canal/canal-server:v1.1.7
  container_name: canal-server
  environment:
    - canal.serverMode=rabbitMQ
    - canal.destinations=digital-mall
    - canal.instance.master.address=postgres:5432
    - canal.instance.dbUsername=postgres
    - canal.instance.dbPassword=123456
    - canal.instance.filter.regex=item\\.item
    - canal.mq.servers=rabbitmq:5672
    - canal.mq.exchange=digital-mall.sync
    - canal.mq.topic=item.sync.routing
  ports:
    - "11111:11111"
  depends_on:
    - postgres
    - rabbitmq
```

### 3.2 ES Dockerfile

`infrastructure/elasticsearch/Dockerfile`:

```dockerfile
FROM elasticsearch:8.16.0
RUN elasticsearch-plugin install --batch https://get.infini.cloud/elasticsearch/analysis-ik/8.16.0
RUN elasticsearch-plugin install --batch https://get.infini.cloud/elasticsearch/analysis-pinyin/8.16.0
```

### 3.3 Java 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

### 3.4 item-service 配置

```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
    connection-timeout: 5s
    socket-timeout: 30s
```

---

## 4. ES 索引

### 4.1 索引名: `digital_mall_items`

| 配置 | 值 |
|------|-----|
| 分片 | 1 |
| 副本 | 1 |
| 索引分词 | ik_smart |
| 搜索分词 | ik_max_word |
| 拼音 | pinyin |

### 4.2 ItemDocument 字段

```
id          Long      ID
name        text      ik_smart + name.pinyin(pinyin) + name.keyword
brand       keyword   筛选 + 聚合
category    keyword   筛选 + 聚合
price       integer   范围过滤 + 排序
stock       integer
image       keyword   index=false
spec        text      ik_smart
sold        integer   排序
commentCount integer
isAD        boolean
status      integer   过滤 (只查 status=1)
createTime  date
updateTime  date
```

### 4.3 搜索 DSL

```json
{
  "query": {
    "bool": {
      "should": [
        { "match": { "name": { "query": "华为", "boost": 3 } } },
        { "match": { "name.pinyin": { "query": "华为", "boost": 1.5 } } },
        { "match": { "brand": { "query": "华为", "boost": 2 } } },
        { "match": { "spec": { "query": "华为", "boost": 0.5 } } }
      ],
      "filter": [
        { "term": { "status": 1 } }
      ]
    }
  },
  "highlight": {
    "fields": {
      "name": { "pre_tags": ["<mark>"], "post_tags": ["</mark>"] }
    }
  },
  "aggs": {
    "categories": { "terms": { "field": "category" } },
    "brands": { "terms": { "field": "brand", "size": 20 } },
    "price_ranges": {
      "range": {
        "field": "price",
        "ranges": [
          { "key": "100以下", "to": 10000 },
          { "key": "100-299", "from": 10000, "to": 30000 },
          { "key": "300-599", "from": 30000, "to": 60000 },
          { "key": "600-899", "from": 60000, "to": 90000 },
          { "key": "900-1599", "from": 90000, "to": 160000 },
          { "key": "1600以上", "from": 160000 }
        ]
      }
    }
  }
}
```

weight: name(3) > brand(2) > name.pinyin(1.5) > spec(0.5)

---

## 5. 数据同步

### 5.1 Canal → RabbitMQ → ES

```
PG item.item → Canal binlog → RabbitMQ (exchange: digital-mall.sync, queue: item.sync.queue)
→ ItemSyncService:
    INSERT status=1 → ES save
    INSERT status!=1 → skip
    UPDATE 1→非1 → ES delete
    UPDATE 非1→1 → ES save
    UPDATE 1→1   → ES save
    DELETE → ES delete
```

### 5.2 全量同步

`POST /api/search/admin/sync-all` — 分页扫 `item.item WHERE status=1` (每批500)，saveAll 到 ES

---

## 6. Java 侧

### 6.1 新增文件

```
item-service/src/main/java/com/digital/mall/item/
├── domain/
│   ├── doc/ItemDocument.java
│   └── dto/{SearchSuggestionDTO, SearchFiltersDTO}.java
├── repository/ItemDocumentRepository.java   (ElasticsearchRepository)
├── service/
│   ├── SearchService.java                   (接口)
│   ├── impl/ESSearchServiceImpl.java        (ES 实现 + 降级)
│   └── ItemSyncService.java                 (Canal 消费 + 全量同步)
└── controller/SearchController.java         (重写)
```

### 6.2 SearchService

```java
public interface SearchService {
    PageDTO<ItemDocument> search(ItemPageQuery query);
    List<SearchSuggestionDTO> suggest(String keyword);
    SearchFiltersDTO getFilters(String keyword);
    void syncAllFromDatabase();
}
```

### 6.3 API

```
GET  /search/list      → search(query)      搜索+筛选+排序+分页
GET  /search/suggest   → suggest(keyword)   搜索建议 (最多10条)
GET  /search/filters   → getFilters(keyword) 聚合筛选统计
POST /search/admin/sync-all → syncAll()     全量同步
```

### 6.4 降级

```java
try { return doSearch(query); }
catch (Exception e) {
    log.error("ES 搜索失败，降级到 SQL LIKE", e);
    return fallbackToSql(query);
}
```

---

## 7. 前端改造

### 7.1 改造清单

| 改动 | 原来 | 改成 |
|------|------|------|
| 搜索接口 | `GET /items/page` | `GET /search/list` |
| 分类接口 | `GET /items/categories` (404) | `GET /search/filters` → categories |
| 品牌接口 | `GET /items/brands` (404) | `GET /search/filters` → brands |
| 主页跳转参数 | `?keyword=` | `?key=` |
| 搜索参数名 | 前端 `name`，后端 `key` | 统一 `key` |
| 排序字段 | `sortBy=sales` | `sortBy=sold` |
| 分类 fallback | 7个 | 3个: 手机、平板、电脑 |
| 搜索建议 | 无 | 新增下拉（300ms 防抖） |
| 关键词高亮 | 无 | `<mark>` v-html 渲染 |
| 聚合统计 | 无 | 标签显示命中数 "华为 (23)" |

### 7.2 参数对照

```
前端 keyword → 后端 key
前端 selectedCategory → 后端 category
前端 selectedBrand → 后端 brand
前端 priceMin → 后端 minPrice
前端 priceMax → 后端 maxPrice
前端 sortBy → 后端 sortBy ("sold" / "price" / "")
前端 isAsc → 后端 isAsc
```

### 7.3 涉及文件

| 文件 | 改动 |
|------|------|
| `index.html` | 搜索跳转参数 `keyword` → `key` |
| `search.html` | 接口地址、参数名、suggest下拉、高亮、聚合统计、fallback分类 |

---

## 8. 种子数据扩展

平板和电脑数据追加到 `infrastructure/sql/seed/item-phone-data.sql` 末尾。

### 8.1 平板 (~30条, category="平板")

| 品牌 | 条数 | 型号示例 |
|------|:---:|------|
| Apple | 10 | iPad Pro M4 / iPad Air M2 / iPad mini 7 |
| 华为 | 10 | MatePad Pro 13.2 / MatePad Air / MatePad SE |
| 小米 | 5 | 小米平板 7 Pro / 小米平板 6S Pro |
| Samsung | 5 | Galaxy Tab S10 Ultra / Tab S9 FE |

### 8.2 电脑 (~20条, category="电脑")

| 品牌 | 条数 | 型号示例 |
|------|:---:|------|
| Apple | 5 | MacBook Pro 14 M4 / MacBook Air 15 M3 |
| 华为 | 5 | MateBook X Pro / MateBook D 16 |
| 联想 | 5 | ThinkPad X1 Carbon / Yoga Pro 14s |
| Dell | 5 | XPS 14 / Inspiron 16 Plus |

### 8.3 总计

手机~150 + 平板~30 + 电脑~20 ≈ 200条

---

## 9. 安全

Gateway `/search/**` 已配置公开访问，无需改动。
`/search/admin/sync-all` 内部调用，实施阶段确定权限控制方式。

---

## 10. 实施顺序

| 步骤 | 内容 | 依赖 |
|:----:|------|------|
| 1 | Canal + PG 兼容性验证 (关键) | 无 |
| 2 | ES Dockerfile + compose 更新 | 无 |
| 3 | 种子数据: 平板+电脑追加 | 无 |
| 4 | ItemDocument + Repository | 2 |
| 5 | SearchService + ES 实现 + 降级 | 4 |
| 6 | SearchController 重写 | 5 |
| 7 | ItemSyncService (Canal消费+全量同步) | 1,2,4 |
| 8 | search.html + index.html 改造 | 6,7 |
| 9 | 端到端联调 | 全部 |

---

## 11. 风险 & 备选

| 风险 | 概率 | 影响 | 备选 |
|------|:----:|:----:|------|
| Canal 不支持 PG | 高 | 同步方案需换 | Spring 事件 + MQ 手动发 (改 ItemController CUD) |
| ES 内存不够 | 低 | 开发卡顿 | 限制 heap 512m |
| IK/Pinyin 版本不匹配 | 低 | ES 启动失败 | 确认版本后手动指定 URL |
| ES 不可用 | 极低 | 搜索报错 | 自动降级 SQL LIKE |
