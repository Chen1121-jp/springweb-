package com.digital.mall.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeckillItemDTO {
    private Long id;
    private Integer seckillPrice;
    private Integer stock;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer maxPurchase;
}
