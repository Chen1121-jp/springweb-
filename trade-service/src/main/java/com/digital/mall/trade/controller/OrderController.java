package com.digital.mall.trade.controller;

import com.digital.mall.common.domain.Result;
import com.digital.mall.common.utils.BeanUtils;
import com.digital.mall.common.utils.UserContext;
import com.digital.mall.trade.domain.dto.OrderFormDTO;
import com.digital.mall.trade.domain.po.Order;
import com.digital.mall.trade.domain.vo.OrderVO;
import com.digital.mall.trade.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单管理接口")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @Operation(summary = "根据id查询订单")
    @GetMapping("/{id}")
    public Result<OrderVO> queryOrderById(@Parameter(description = "订单id") @PathVariable("id") Long orderId) {
        return Result.ok(BeanUtils.copyBean(orderService.getById(orderId), OrderVO.class));
    }

    @Operation(summary = "查询当前用户订单列表")
    @GetMapping
    public Result<List<OrderVO>> queryUserOrders() {
        List<Order> orders = orderService.query()
                .eq("user_id", UserContext.getUser())
                .orderByDesc("create_time")
                .list();
        return Result.ok(BeanUtils.copyList(orders, OrderVO.class));
    }

    @Operation(summary = "创建订单")
    @PostMapping
    public Result<Long> createOrder(@RequestBody OrderFormDTO orderFormDTO) {
        return Result.ok(orderService.createOrder(orderFormDTO));
    }

    @Operation(summary = "标记订单已支付")
    @PutMapping("/{orderId}")
    public Result<Void> markOrderPaySuccess(@Parameter(description = "订单id") @PathVariable("orderId") Long orderId) {
        orderService.markOrderPaySuccess(orderId);
        return Result.ok();
    }

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.ok("trade-service is running");
    }
}
