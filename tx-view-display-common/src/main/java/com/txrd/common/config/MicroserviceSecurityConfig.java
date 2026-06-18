package com.txrd.common.config;

import com.txrd.common.filter.MicroserviceGatewayAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MicroserviceSecurityConfig {

    @Autowired
    private MicroserviceGatewayAuthFilter microserviceGatewayAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 白名单：直接放行（与网关保持一致）
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/doc.html").permitAll()
                        .requestMatchers("/swagger-resources").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/test").permitAll()
                        .requestMatchers(req -> "gateway".equals(req.getHeader("X-Gateway-Source"))).permitAll()
                        .requestMatchers(req -> "feign".equals(req.getHeader("X-Feign-Source"))).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(microserviceGatewayAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .anonymous(anonymous -> anonymous.disable())  // ← 禁用匿名认证！
                .httpBasic(httpBasic -> httpBasic.disable())  // ← 禁用！
                .formLogin(formLogin -> formLogin.disable()); // ← 同时禁用表单登录

        return http.build();
    }
}
