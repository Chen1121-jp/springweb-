package com.digital.mall.item.domain.query;

import com.digital.mall.common.domain.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分页查询条件
 * <p>
 * 封装搜索/查询时的多条件组合参数，继承通用分页参数（pageNo、pageSize、sortBy、isAsc）。
 * 既是 ES 搜索的入参，也是 MySQL 降级查询和 Feign 远程调用的入参。
 *
 * @author digital-mall
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "商品分页查询条件")
public class ItemPageQuery extends PageQuery {
    /** 搜索关键词，支持商品名、品牌、规格的多字段加权匹配 */
    @Schema(description = "搜索关键字")
    private String key;
    /** 商品分类，ES中为Keyword类型，精确匹配 */
    @Schema(description = "商品分类")
    private String category;
    /** 商品品牌，ES中为Keyword类型，精确匹配 */
    @Schema(description = "商品品牌")
    private String brand;
    /** 价格区间下限（含），为null时不设下限 */
    @Schema(description = "价格最小值")
    private Integer minPrice;
    /** 价格区间上限（含），为null时不设上限 */
    @Schema(description = "价格最大值")
    private Integer maxPrice;
}
