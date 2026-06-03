package com.digital.mall.api.query;

import com.digital.mall.common.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemPageQuery extends PageQuery {
    private String key;
    private String brand;
    private String category;
    private Integer minPrice;
    private Integer maxPrice;
}
