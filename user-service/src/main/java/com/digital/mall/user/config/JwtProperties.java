package com.digital.mall.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

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
