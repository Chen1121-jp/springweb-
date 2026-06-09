package com.digital.mall.common.constants;

public interface MQConstants {
    String DELAY_EXCHANGE_NAME = "delay.order.direct";
    // seckill
    String SECKILL_DELAY_QUEUE = "seckill.delay.queue";
    String SECKILL_DELAY_KEY  = "seckill.delay";

    // trade
    String TRADE_DELAY_QUEUE = "trade.delay.queue";
    String TRADE_DELAY_KEY   = "trade.delay";
}
