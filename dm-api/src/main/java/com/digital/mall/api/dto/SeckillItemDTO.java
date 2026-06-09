package com.digital.mall.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeckillItemDTO {
    private Long id;
    /** 商品名称 */
    private String name;
    /** 商品图片URL */
    private String image;
    /** 商品原价（分） */
    private Integer originalPrice;
    /** 秒杀价（分） */
    private Integer seckillPrice;
    /** 秒杀库存 */
    private Integer stock;
    /** 秒杀开始时间 */
    private LocalDateTime beginTime;
    /** 秒杀结束时间 */
    private LocalDateTime endTime;
    /** 限购数量 */
    private Integer maxPurchase;
}
