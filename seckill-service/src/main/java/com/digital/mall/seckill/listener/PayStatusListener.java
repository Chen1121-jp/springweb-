package com.digital.mall.seckill.listener;

  import com.digital.mall.seckill.service.ISeckillOrderService;
  import lombok.RequiredArgsConstructor;
  import org.springframework.amqp.rabbit.annotation.Exchange;
  import org.springframework.amqp.rabbit.annotation.Queue;
  import org.springframework.amqp.rabbit.annotation.QueueBinding;
  import org.springframework.amqp.rabbit.annotation.RabbitListener;
  import org.springframework.stereotype.Component;

  @Component
  @RequiredArgsConstructor
  public class PayStatusListener {

      private final ISeckillOrderService seckillOrderService;

      @RabbitListener(bindings = @QueueBinding(
              value = @Queue(name = "seckill.pay.success.queue", durable = "true"),
              exchange = @Exchange(name = "pay.direct"),
              key = "pay.success"
      ))
      public void listenPaySuccess(Long orderId) {
          seckillOrderService.markOrderPaySuccess(orderId);
      }
  }