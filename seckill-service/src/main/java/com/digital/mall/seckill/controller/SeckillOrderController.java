package com.digital.mall.seckill.controller;

import com.digital.mall.common.domain.Result;
import com.digital.mall.seckill.service.ISeckillOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "秒杀订单相关接口")
@RestController
@RequestMapping("/seckillOrder")
@RequiredArgsConstructor
public class SeckillOrderController {

    private final ISeckillOrderService seckillOrderService;

    @Operation(summary = "秒杀商品接口")
    @PostMapping("/{id}")
    public Long seckillVoucher(@PathVariable("id") Long itemId) {
        return seckillOrderService.seckillOrder(itemId);
    }

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("seckill-service is running");
    }
}
