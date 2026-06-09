package com.digital.mall.item.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqSyncConfig {

    public static final String EXCHANGE = "digital-mall.sync";
    public static final String QUEUE = "item.sync.queue";
    public static final String ROUTING_KEY = "item.sync";

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue syncQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding syncBinding() {
        return BindingBuilder.bind(syncQueue()).to(syncExchange()).with(ROUTING_KEY);
    }
}
