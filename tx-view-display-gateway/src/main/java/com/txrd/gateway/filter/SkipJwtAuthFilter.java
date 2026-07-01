package com.txrd.gateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class SkipJwtAuthFilter implements WebFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 定义白名单
    private static final String[] WHITE_LIST = {
            "/auth/**",
            "/*/test",
            "/favicon.ico",
            "/doc.html",
            "/swagger-resources/&zwnj;**",
            "/swagger-ui/**&zwnj;",
            "/v3/api-docs/&zwnj;**",
            "/webjars/**&zwnj;"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isWhiteList(path)) {
            // 创建一个修改后的请求，移除 Authorization 头
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("Authorization", (String) null) // 移除头
                    .build();

            // 使用修改后的请求继续过滤链
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        }

        return chain.filter(exchange);
    }

    private boolean isWhiteList(String path) {
        for (String pattern : WHITE_LIST) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
