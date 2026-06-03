package com.digital.mall.seckill.listener;

import com.digital.mall.common.constants.MQConstants;
import com.digital.mall.seckill.domain.SeckillItem;
import com.digital.mall.seckill.domain.SeckillOrder;
import com.digital.mall.seckill.service.ISeckillItemService;
import com.digital.mall.seckill.service.ISeckillOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderDelayMessageListener {

    private static final String STOCK_KEY_PREFIX = "seckill:stock:";
    private static final String ORDER_SET_PREFIX = "seckill:order:";
    private static final long DELETE_INTERVAL_MS = 500;

    private final ISeckillOrderService seckillOrderService;
    private final ISeckillItemService seckillItemService;
    private final StringRedisTemplate stringRedisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstants.DELAY_ORDER_QUEUE_NAME),
            exchange = @Exchange(name = MQConstants.DELAY_EXCHANGE_NAME, delayed = "true"),
            key = MQConstants.DELAY_ORDER_KEY
    ))
    public void listenOrderDelayMessage(Long seckillOrderId) {
        log.info("订单延迟队列监听到延迟消息：{}", seckillOrderId);

        // 1. 查询订单
        SeckillOrder seckillOrder = seckillOrderService.getById(seckillOrderId);

        // 2. 检测订单状态，判断是否已支付
        if (seckillOrder != null && seckillOrder.getStatus() != 1) {
            Long itemId = seckillOrder.getItemId();
            String stockKey = STOCK_KEY_PREFIX + itemId;
            String orderKey = ORDER_SET_PREFIX + itemId;
            String userId = seckillOrder.getUserId().toString();

            // 3. 回退数据库库存（seckill_item 表 + item 表）
            seckillItemService.returnStock(itemId, 1);

            // 4. 删除订单
            seckillOrderService.removeById(seckillOrderId);

            // 5. 清除 Redis 一人一单资格限制
            stringRedisTemplate.opsForSet().remove(orderKey, userId);

            // 6. 延迟双删：保证 Redis 库存缓存与数据库的一致性
            stringRedisTemplate.delete(stockKey);
            try {
                Thread.sleep(DELETE_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stringRedisTemplate.delete(stockKey);

            // 7. 从数据库重新加载正确的库存值到 Redis
            SeckillItem seckillItem = seckillItemService.getById(itemId);
            if (seckillItem != null) {
                stringRedisTemplate.opsForValue()
                        .set(stockKey, String.valueOf(seckillItem.getStock()));
            }

            log.info("订单 {} 超时未支付，已回滚数据库及 Redis 数据（延迟双删完成）", seckillOrderId);
        }
    }
}
