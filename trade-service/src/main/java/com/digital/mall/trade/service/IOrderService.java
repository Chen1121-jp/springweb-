package com.digital.mall.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.mall.trade.domain.dto.OrderFormDTO;
import com.digital.mall.trade.domain.po.Order;

public interface IOrderService extends IService<Order> {

    Long createOrder(OrderFormDTO orderFormDTO);

    void markOrderPaySuccess(Long orderId);

    void cancelOrder(Long orderId);
}
