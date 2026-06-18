package com.txrd.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Aspect
//@Component
public class PreAuthorizeDebugAspect {

    // 拦截所有带 @PreAuthorize 的方法
    @Around("@annotation(preAuthorize)")
    public Object aroundPreAuthorize(ProceedingJoinPoint joinPoint, PreAuthorize preAuthorize) throws Throwable {
        System.out.println("=== @PreAuthorize AOP 拦截 ===");
        System.out.println("方法: " + joinPoint.getSignature());
        System.out.println("表达式: " + preAuthorize.value());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("当前 Authentication: " + auth);
        System.out.println("当前 Authorities: " + (auth != null ? auth.getAuthorities() : "null"));
        try {
            Object result = joinPoint.proceed();
            System.out.println("=== @PreAuthorize 通过 ===");
            return result;
        } catch (AccessDeniedException e) {
            System.out.println("=== @PreAuthorize 拒绝: " + e.getMessage() + " ===");
            throw e;
        }
    }

    @Before("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void restoreSecurityContext(JoinPoint joinPoint) {
        Authentication current = SecurityContextHolder.getContext().getAuthentication();
        if (current == null || !current.isAuthenticated() || "anonymousUser".equals(current.getPrincipal())) {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                Object authObj = request.getAttribute("AUTHENTICATION");

                if (authObj instanceof UsernamePasswordAuthenticationToken auth) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("=== AOP 恢复 Authentication: " + auth.getName() + " ===");
                }
            }
        }
    }
}
