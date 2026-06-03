package com.digital.mall.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import org.springframework.core.io.Resource;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "dm.jwt")
public class JwtProperties {
    private Resource location;
    private String alias;
    private String password;
    private Duration tokenTTL;
}
