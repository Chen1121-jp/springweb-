package com.digital.mall.api.client;

import com.digital.mall.api.dto.PayOrderDTO;
import com.digital.mall.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("pay-service")
public interface PayClient {
    @GetMapping("/pay-orders/biz/{id}")
    Result<PayOrderDTO> queryPayOrderByBizOrderNo(@PathVariable("id") Long id);
}
