package com.digital.mall.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyPair keyPair() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(jwtProperties.getLocation().getInputStream(),
                    jwtProperties.getPassword().toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(
                    jwtProperties.getAlias(),
                    jwtProperties.getPassword().toCharArray());
            Certificate cert = keyStore.getCertificate(jwtProperties.getAlias());
            PublicKey publicKey = cert.getPublicKey();
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new RuntimeException("加载 JKS 密钥库失败", e);
        }
    }
}
