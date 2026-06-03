package com.digital.mall.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.mall.seckill.domain.SeckillOrder;

public interface ISeckillOrderService extends IService<SeckillOrder> {

    Long seckillOrder(Long itemId);
}
