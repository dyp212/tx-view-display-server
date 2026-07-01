
package com.txrd.gateway.filter;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.AntPathMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txrd.base.result.CommonResult;
import com.txrd.base.security.CustomerAuthenticationToken;
import com.txrd.base.util.I18nUtil;
import com.txrd.base.util.IPUtil;
import com.txrd.base.util.JwtUtil;
import com.txrd.gateway.dto.AccessLogDTO;
import com.txrd.gateway.service.PermissionCacheService;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 此时 Security 已经校验通过，我们可以安全地获取用户信息
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    ServerHttpRequest request = exchange.getRequest();

                    // 1. 提取信息
                    String userId = authentication.getName(); // Subject
                    String account = extractAccount(authentication);
                    String userDetails = extractUserDetails(authentication);

                    // 2. 处理语言
                    String language = resolveLanguage(request);

                    // 3. 构建新请求，透传 Header
                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("userId", userId)
                            .header("account", account)
                            .header("userDetails", userDetails)
                            .header("permissions", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                            .header("traceId", UUID.randomUUID().toString()) // 建议使用 Slf4j MDC 或 SkyWalking ID
                            .header("Accept-Language", language)
                            .header("X-Gateway-Source", "gateway")
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .switchIfEmpty(Mono.defer(()->{
                    ServerHttpRequest request = exchange.getRequest();
                    String language = resolveLanguage(request);
                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("traceId", UUID.randomUUID().toString())
                            .header("Accept-Language", language)
                            .header("X-Gateway-Source", "gateway")
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                }) ); // 理论上不会走到这，因为 Security 已拦截未认证请求
    }

    private String extractAccount(Authentication authentication) {
        if (authentication instanceof CustomerAuthenticationToken customerAuthenticationToken) {
            return customerAuthenticationToken.getAccount();
        }
        return "";
    }

    private String extractUserDetails(Authentication authentication) {
        if (authentication instanceof CustomerAuthenticationToken customerAuthenticationToken) {
            return customerAuthenticationToken.getToken().getClaimAsString("userDetails");
        }
        return "";
    }

    private String resolveLanguage(ServerHttpRequest request) {
        String lang = request.getHeaders().getFirst("Accept-Language");
        if (lang == null) {
            lang = request.getHeaders().getFirst("lang");
        }
        return (lang != null && lang.contains("en")) ? "en-US" : "zh-CN";
    }

    @Override
    public int getOrder() {
        return -99;
    }

}
