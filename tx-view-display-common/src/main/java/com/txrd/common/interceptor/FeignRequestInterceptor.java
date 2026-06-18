package com.txrd.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 1. 从当前请求的 Header 中获取用户信息（网关透传的）
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        template.header("X-Invoke-Type", "INTERNAL");
        template.header("X-Feign-Source", "feign");
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            //
            String traceId = Optional.ofNullable(request.getHeader("traceId")).orElse((String)request.getAttribute("traceId"));

            // 透传用户相关 Header
            String userId = request.getHeader("userId");
            String account = request.getHeader("account");

            if (userId != null) {
                template.header("userId", userId);
            } else {
                template.header("userId", "SYSTEM ");
            }
            if (account != null) {
                template.header("account", account);
            } else {
                template.header("account", "FEIGN");
            }
            if (traceId != null) {
                template.header("traceId", traceId);
            }
        }
    }
}
