package com.digital.mall.trade.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("order_detail")
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private Long orderId;
    private Long itemId;
    private Integer num;
    private String name;
    private String spec;
    private Integer price;
    private String image;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
