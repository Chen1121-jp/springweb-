package com.digital.mall.trade.constants;

/**
 * trade-service 本地 MQ 常量
 * 注意：延时队列相关的常量统一使用 dm-common 中的 {@link com.digital.mall.common.constants.MQConstants}
 */
public interface MqConstants {
    String DELAY_EXCHANGE_NAME = "delay.order.direct";
    String DELAY_ORDER_QUEUE_NAME = "delay.order.queue";
    String DELAY_ORDER_KEY = "delay.order.query";
}
