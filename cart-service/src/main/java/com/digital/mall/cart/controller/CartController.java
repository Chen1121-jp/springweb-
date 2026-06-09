package com.digital.mall.cart.controller;

import com.digital.mall.cart.domain.dto.CartFormDTO;
import com.digital.mall.cart.domain.po.Cart;
import com.digital.mall.cart.domain.vo.CartVO;
import com.digital.mall.cart.service.ICartService;
import com.digital.mall.common.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车相关接口")
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping
    public Result<Void> addItem2Cart(@Valid @RequestBody CartFormDTO cartFormDTO) {
        cartService.addItem2Cart(cartFormDTO);
        return Result.ok();
    }

    @Operation(summary = "更新购物车数据")
    @PutMapping
    public Result<Void> updateCart(@RequestBody Cart cart) {
        cartService.updateById(cart);
        return Result.ok();
    }

    @Operation(summary = "删除购物车中商品")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCartItem(@Parameter(description = "购物车条目id") @PathVariable("id") Long id) {
        cartService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "查询购物车列表")
    @GetMapping
    public Result<List<CartVO>> queryMyCarts() {
        return Result.ok(cartService.queryMyCarts());
    }

    @Operation(summary = "批量删除购物车中商品")
    @DeleteMapping
    public Result<Void> deleteCartItemByIds(@Parameter(description = "购物车条目id集合") @RequestParam("ids") List<Long> ids) {
        cartService.removeByItemIds(ids);
        return Result.ok();
    }

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("cart-service is running");
    }
}
