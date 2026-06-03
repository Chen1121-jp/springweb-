package com.digital.mall.user.utils;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.digital.mall.common.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTool {

    private final JWTSigner jwtSigner;

    public JwtTool(KeyPair keyPair) {
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }

    /**
     * 创建 access-token
     *
     * @param userId 用户 ID
     * @param ttl    过期时间
     * @return JWT token 字符串
     */
    public String createToken(Long userId, Duration ttl) {
        return JWT.create()
                .setPayload("userId", userId)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    /**
     * 解析 token
     *
     * @param token JWT token
     * @return 用户 ID
     */
    public Long parseToken(String token) {
        if (token == null) {
            throw new UnauthorizedException("未登录");
        }
        // 1. 校验并解析 jwt
        JWT jwt;
        try {
            jwt = JWT.of(token).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new UnauthorizedException("无效的token", e);
        }
        // 2. 校验 jwt 是否有效
        if (!jwt.verify()) {
            throw new UnauthorizedException("无效的token");
        }
        // 3. 校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            throw new UnauthorizedException("token已经过期");
        }
        // 4. 数据格式校验
        Object userPayload = jwt.getPayload("userId");
        if (userPayload == null) {
            throw new UnauthorizedException("无效的token");
        }
        // 5. 数据解析
        try {
            return Long.valueOf(userPayload.toString());
        } catch (RuntimeException e) {
            throw new UnauthorizedException("无效的token");
        }
    }
}
