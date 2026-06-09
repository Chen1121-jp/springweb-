package com.digital.mall.item.repository;

import com.digital.mall.item.domain.doc.ItemDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 商品文档 ES 数据访问层
 * <p>
 * 继承 Spring Data Elasticsearch 的 ElasticsearchRepository，
 * 提供对 digital_mall_items 索引的 CRUD 操作。
 *
 * @author digital-mall
 */
@Repository
public interface ItemDocumentRepository extends ElasticsearchRepository<ItemDocument, Long> {
}
