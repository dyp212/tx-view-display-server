package com.txrd.gateway.config;

import cn.hutool.json.JSONUtil;
import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        if (authException instanceof InvalidBearerTokenException) {
            String exMessage = authException.getMessage();
            if (exMessage != null && exMessage.contains("expired")) {
                CommonResult.get(401, "登录已过期，请重新登录", null);
            } else {
                CommonResult.get(401, "令牌无效或已失效", null);
            }
        } else if (authException instanceof InsufficientAuthenticationException) {
            CommonResult.get(401, "请先登录", null);
        } else {
            CommonResult.get(401, "认证失败", null);
        }

        String body = JSONUtil.toJsonStr(CommonResult.get(401, I18nUtil.getMessage("token.expires"), null));
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
