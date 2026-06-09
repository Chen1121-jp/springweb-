package com.digital.mall.seckill.controller;

import com.digital.mall.common.domain.Result;
import com.digital.mall.seckill.domain.SeckillItem;
import com.digital.mall.seckill.service.ISeckillItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "秒杀商品相关接口")
@RestController
@RequestMapping("/seckillItem")
@RequiredArgsConstructor
public class SeckillItemController {

    private final ISeckillItemService seckillItemService;

    @Operation(summary = "添加秒杀商品接口")
    @PostMapping("/{id}")
    public Result<Void> addVoucher(@PathVariable("id") Long id) {
        seckillItemService.saveWithItemId(id);
        return Result.ok();
    }

    @Operation(summary = "查询所有秒杀商品")
    @GetMapping
    public Result<List<SeckillItem>> querySeckillItems() {
        return Result.ok(seckillItemService.list());
    }
}
