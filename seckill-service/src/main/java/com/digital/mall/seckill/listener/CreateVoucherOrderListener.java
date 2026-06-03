package com.digital.mall.seckill.listener;

import com.digital.mall.api.client.ItemClient;
import com.digital.mall.seckill.domain.SeckillOrder;
import com.digital.mall.seckill.service.ISeckillItemService;
import com.digital.mall.seckill.service.ISeckillOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateVoucherOrderListener {

    private final ISeckillOrderService seckillOrderService;
    private final ISeckillItemService seckillItemService;
    private final ItemClient itemClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "orderItem.lazy.queue", durable = "true",
                    arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "orderItem.lazy.direct"),
            key = "create.success"
    ))
    public void listenPaySuccess(SeckillOrder seckillOrder) {
        log.info("接收到秒杀下单消息，订单ID：{}", seckillOrder.getId());

        // 1. 幂等性校验：判断订单是否已存在
        SeckillOrder oldOrder = seckillOrderService.getById(seckillOrder.getId());
        if (oldOrder != null) {
            log.warn("订单 {} 已存在，忽略重复消息", seckillOrder.getId());
            return;
        }

        try {
            // 2. 保存订单到数据库
            seckillOrderService.save(seckillOrder);

            // 3. 扣减数据库库存（seckill_item 表）
            boolean success = seckillItemService.update()
                    .setSql("stock = stock - 1")
                    .eq("id", seckillOrder.getItemId())
                    .gt("stock", 0)
                    .update();
            // 4. 扣减 item 库存
            itemClient.reduceStock(seckillOrder.getItemId());

            if (!success) {
                log.error("数据库库存扣减失败，订单ID：{}", seckillOrder.getId());
            }
        } catch (Exception e) {
            log.error("处理下单消息异常", e);
            throw e;
        }
    }
}
