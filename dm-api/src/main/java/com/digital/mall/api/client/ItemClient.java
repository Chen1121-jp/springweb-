package com.digital.mall.api.client;

import com.digital.mall.api.dto.ItemDTO;
import com.digital.mall.api.dto.OrderDetailDTO;
import com.digital.mall.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(value = "item-service", fallbackFactory = com.digital.mall.api.client.fallback.ItemClientFallback.class)
public interface ItemClient {
    @GetMapping("/items")
    Result<List<ItemDTO>> queryItemByIds(@RequestParam("ids") Collection<Long> ids);

    @PutMapping("/items/stock/deduct")
    void deductStock(@RequestBody List<OrderDetailDTO> items);

    @GetMapping("/items/{id}")
    Result<ItemDTO> queryItemById(@PathVariable("id") Long id);

    @PostMapping("/items/stock/{id}")
    void returnStock(@PathVariable("id") Long id);

    @PostMapping("/items/reduce/{id}")
    void reduceStock(@PathVariable("id") Long id);
}
