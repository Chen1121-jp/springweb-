package com.digital.mall.api.client;

import com.digital.mall.api.dto.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient("trade-service")
public interface TradeClient {
    @PutMapping("/orders/{orderId}")
    void markOrderPaySuccess(@PathVariable("orderId") Long orderId);

    @GetMapping("/orders/{id}")
    OrderVO queryOrderById(@PathVariable("id") Long orderId);

    @GetMapping("/orders")
    List<OrderVO> queryUserOrders();
}
