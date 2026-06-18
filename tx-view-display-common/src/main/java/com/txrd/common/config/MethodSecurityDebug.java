package com.txrd.common.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;

//@Component
public class MethodSecurityDebug implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MethodSecurityInterceptor) {
            MethodSecurityInterceptor interceptor = (MethodSecurityInterceptor) bean;

            // 包装原来的 AccessDecisionManager
            AccessDecisionManager original = interceptor.getAccessDecisionManager();
            interceptor.setAccessDecisionManager(new AccessDecisionManager() {
                @Override
                public void decide(Authentication authentication, Object object,
                                   Collection<ConfigAttribute> configAttributes)
                        throws AccessDeniedException, InsufficientAuthenticationException {

                    System.out.println("=== MethodSecurityInterceptor.decide 执行 ===");
                    System.out.println("Authentication: " + authentication);
                    System.out.println("Object: " + object);
                    System.out.println("ConfigAttributes: " + configAttributes);

                    original.decide(authentication, object, configAttributes);
                }

                @Override
                public boolean supports(ConfigAttribute attribute) {
                    return original.supports(attribute);
                }

                @Override
                public boolean supports(Class<?> clazz) {
                    return original.supports(clazz);
                }
            });
        }
        return bean;
    }
}
