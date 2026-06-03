package com.digital.mall.api.client;

import com.digital.mall.api.dto.SeckillItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("seckill-service")
public interface SeckillClient {
    @GetMapping("/seckillItem")
    List<SeckillItemDTO> querySeckillItems();
}
