package com.digital.mall.gateway.filter;

import com.digital.mall.common.exception.UnauthorizedException;
import com.digital.mall.gateway.config.AuthProperties;
import com.digital.mall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        boolean optional = isExclude(exchange.getRequest().getPath().toString());
        return resolveUser(exchange, chain, optional);
    }

    /**
     * 解析 JWT 并设置 user-info 头。
     * @param optional 为 true 时 token 缺失或无效不拒绝请求（白名单路径）
     */
    private Mono<Void> resolveUser(ServerWebExchange exchange, GatewayFilterChain chain, boolean optional) {
        ServerHttpRequest request = exchange.getRequest();

        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        if (headers != null && !headers.isEmpty()) {
            token = headers.get(0);
        }

        if (token == null) {
            if (optional) {
                return chain.filter(exchange);
            }
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        try {
            Long userId = jwtTool.parseToken(token);
            ServerWebExchange swe = exchange.mutate()
                    .request(builder -> builder.header("user-info", userId.toString()))
                    .build();
            return chain.filter(swe);
        } catch (UnauthorizedException e) {
            if (optional) {
                return chain.filter(exchange);
            }
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    private boolean isExclude(String path) {
        List<String> excludePaths = authProperties.getExcludePaths();
        if (excludePaths == null) {
            return false;
        }
        for (String pathPattern : excludePaths) {
            if (antPathMatcher.match(pathPattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
