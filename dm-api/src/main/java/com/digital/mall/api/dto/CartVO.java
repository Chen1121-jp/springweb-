package com.digital.mall.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "购物车视图")
public class CartVO {
    private Long id;
    private Long userId;
    private Long itemId;
    private Integer num;
    private String name;
    private String spec;
    private Integer price;
    private String image;
    private Integer status;
    private Integer stock;
    private Integer newPrice;
}
