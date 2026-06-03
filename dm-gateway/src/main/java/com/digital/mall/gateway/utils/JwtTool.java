package com.digital.mall.gateway.utils;

import com.digital.mall.common.exception.UnauthorizedException;
import com.digital.mall.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.KeyStore;
import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class JwtTool {

    private final JwtProperties jwtProperties;

    public Long parseToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException("未登录");
        }
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(jwtProperties.getLocation().getInputStream(),
                    jwtProperties.getPassword().toCharArray());
            PublicKey publicKey = keyStore.getCertificate(jwtProperties.getAlias()).getPublicKey();

            Claims claims = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            throw new UnauthorizedException("token无效");
        }
    }
}
