package com.txrd.common.filter;

import cn.hutool.core.lang.UUID;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class MicroserviceGatewayAuthFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String userId = request.getHeader("userId");
        String traceId = request.getHeader("traceId");
        String invokeType = request.getHeader("X-Invoke-Type");
        String feignSource = request.getHeader("X-Feign-Source");
        String source = request.getHeader("X-Gateway-Source");
        if(StringUtils.isBlank(traceId)) {
            request.setAttribute("traceId", UUID.randomUUID().toString());
        }

        // 情况1：正常用户请求（有 userId，来自网关认证）
        if (userId != null && invokeType == null && "gateway".equals(source)) {
            String permissions = request.getHeader("permissions");
            List<SimpleGrantedAuthority> authorityList = Collections.emptyList();
            if(StringUtils.isNotBlank(permissions)) {
                authorityList = Arrays.stream(permissions.split(",")).map(String::trim).filter(StringUtils::isNotBlank).map(SimpleGrantedAuthority::new).toList();
            }
            // 构建正常用户认证
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, authorityList);
            request.setAttribute("AUTHENTICATION", auth);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            request.setAttribute(RequestAttributeSecurityContextRepository.DEFAULT_REQUEST_ATTR_NAME, context);
            SecurityContextHolder.setContext(context);
            chain.doFilter(request, response);
            return;
        }

        // 情况2：内部系统调用（SYSTEM + INTERNAL 标记）
        if ("INTERNAL".equals(invokeType) || "FEIGN".equals(feignSource)) {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_SYSTEM")));
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.setAttribute(RequestAttributeSecurityContextRepository.DEFAULT_REQUEST_ATTR_NAME, SecurityContextHolder.getContext());
            chain.doFilter(request, response);
            return;
        }

        // 情况3：无认证，拒绝
        chain.doFilter(request, response);
    }


}
