package com.digital.mall.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.mall.common.config.RedisIdWorker;
import com.digital.mall.common.constants.MQConstants;
import com.digital.mall.common.exception.BizIllegalException;
import com.digital.mall.common.utils.UserContext;
import com.digital.mall.seckill.domain.SeckillOrder;
import com.digital.mall.seckill.mapper.SeckillOrderMapper;
import com.digital.mall.seckill.service.ISeckillOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    private final RedisIdWorker redisIdWorker;
    private final StringRedisTemplate stringRedisTemplate;
    private final RabbitTemplate rabbitTemplate;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @Override
    public Long seckillOrder(Long itemId) {
        Long userId = UserContext.getUser();
        log.info("用户:{} 正在秒杀商品:{}", userId, itemId);

        // 1. 全局唯一订单 ID
        long orderId = redisIdWorker.nextId("order");

        // 2. 执行 Lua 脚本（原子：判库存 + 判一人一单 + 扣库存 + 记录用户）
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                itemId.toString(), userId.toString(), String.valueOf(orderId)
        );
        log.info("Lua 脚本执行结果:{}", result);

        int r = result != null ? result.intValue() : -1;
        if (r != 0) {
            throw new BizIllegalException(r == 1 ? "库存不足" : "您已参与过此秒杀");
        }

        // 3. 构建秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setId(orderId);
        seckillOrder.setUserId(userId);
        seckillOrder.setItemId(itemId);
        log.info("秒杀订单创建成功:{}", seckillOrder);

        // 4. 异步落库（MQ 解耦）
        try {
            rabbitTemplate.convertAndSend("orderItem.lazy.direct", "create.success", seckillOrder);
            // 5. 延迟消息（超时未支付自动回滚）
            rabbitTemplate.convertAndSend(
                    MQConstants.DELAY_EXCHANGE_NAME,
                    MQConstants.DELAY_ORDER_KEY,
                    seckillOrder.getId(),
                    message -> {
                        message.getMessageProperties().setDelay(1000 * 30);
                        return message;
                    });
            log.info("延迟消息已发送");
        } catch (Exception e) {
            log.error("订单消息发送失败:{}", seckillOrder.getId(), e);
        }

        return orderId;
    }
}
