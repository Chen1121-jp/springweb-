package com.digital.mall.seckill.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("seckill_item")
public class SeckillItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /** 商品名称（冗余自 item 表） */
    private String name;
    /** 商品图片URL（冗余自 item 表） */
    private String image;
    /** 商品原价（分）（冗余自 item 表） */
    private Integer originalPrice;
    /** 秒杀价（分） */
    private Integer seckillPrice;
    /** 秒杀库存 */
    private Integer stock;
    /** 秒杀开始时间 */
    private LocalDateTime beginTime;
    /** 秒杀结束时间 */
    private LocalDateTime endTime;
    /** 限购数量（默认1） */
    private Integer maxPurchase;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
