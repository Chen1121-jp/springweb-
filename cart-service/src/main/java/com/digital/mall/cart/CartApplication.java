package com.digital.mall.cart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.digital.mall.cart.mapper")
@EnableFeignClients(basePackages = "com.digital.mall.api.client", defaultConfiguration = com.digital.mall.api.config.DefaultFeignConfig.class)
@SpringBootApplication
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }
}
