package com.digital.mall.item.domain.doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

/**
 * 商品 ES 索引文档
 * <p>
 * 映射到 Elasticsearch 的 digital_mall_items 索引，
 * 使用 IK 中文分词 + lowercase 过滤器实现中文全文检索。
 * <p>
 * 索引策略：
 * <ul>
 *   <li>name：Text 类型，写入用 ik_smart_lower（粗粒度分词），搜索用 ik_max_word_lower（细粒度分词），提高召回率</li>
 *   <li>brand / category：Keyword 类型，精确匹配</li>
 *   <li>spec：Text 类型，参与关键词搜索但权重较低（0.5）</li>
 *   <li>price / sold：Integer 类型，支持排序和范围查询</li>
 *   <li>image：index=false，不参与索引仅存储</li>
 * </ul>
 *
 * @author digital-mall
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "digital_mall_items")
@Setting(settingPath = "elasticsearch/item-index-settings.json")
public class ItemDocument {

    /** 商品主键ID，与数据库一致 */
    @Id
    private Long id;

    /** 商品名称，核心搜索字段，权重3.0 */
    @Field(type = FieldType.Text, analyzer = "ik_smart_lower", searchAnalyzer = "ik_max_word_lower")
    private String name;

    /** 品牌名称，精确匹配，权重2.0 */
    @Field(type = FieldType.Keyword)
    private String brand;

    /** 商品分类，精确匹配 */
    @Field(type = FieldType.Keyword)
    private String category;

    /** 商品价格（分） */
    @Field(type = FieldType.Integer)
    private Integer price;

    /** 库存数量 */
    @Field(type = FieldType.Integer)
    private Integer stock;

    /** 商品图片URL，不参与索引 */
    @Field(type = FieldType.Keyword, index = false)
    private String image;

    /** 规格参数，参与关键词搜索，权重0.5 */
    @Field(type = FieldType.Text, analyzer = "ik_smart_lower")
    private String spec;

    /** 销量，支持排序 */
    @Field(type = FieldType.Integer)
    private Integer sold;

    /** 评论数量 */
    @Field(type = FieldType.Integer)
    private Integer commentCount;

    /** 是否为广告推广商品 */
    @Field(type = FieldType.Boolean)
    private Boolean isAD;

    /** 商品状态：1-上架，2-下架，3-删除。搜索仅返回 status=1 */
    @Field(type = FieldType.Integer)
    private Integer status;

    /** 创建时间 */
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 最后更新时间，默认排序字段 */
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
