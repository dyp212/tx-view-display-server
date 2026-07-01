package com.txrd.gateway.config;

import com.nimbusds.jose.jwk.RSAKey;
import com.txrd.gateway.filter.SkipJwtAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    // 注入自定义的授权管理器（见第三步）
    private final CustomAuthorizationManager customAuthorizationManager;
    // 注入自定义的 JWT 转换器（见第二步）
    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    private final SkipJwtAuthFilter skipJwtAuthFilter;


    public SecurityConfig(CustomAuthorizationManager customAuthorizationManager,
                          CustomJwtAuthenticationConverter customJwtAuthenticationConverter,
                          SkipJwtAuthFilter skipJwtAuthFilter) {
        this.customAuthorizationManager = customAuthorizationManager;
        this.customJwtAuthenticationConverter = customJwtAuthenticationConverter;
        this.skipJwtAuthFilter = skipJwtAuthFilter;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                // 1. 禁用 CSRF (因为是无状态 JWT)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // 2. 配置所有请求都需要认证
                .authorizeExchange(exchanges -> exchanges
                        // 1. 白名单：直接放行，不进行任何 JWT 校验
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/*/test").permitAll()
                        .pathMatchers("favicon.ico").permitAll()
                        .pathMatchers("/doc.html").permitAll()
                        .pathMatchers("/swagger-resources").permitAll()
                        .pathMatchers("/swagger-ui/**").permitAll()
                        .pathMatchers("/v3/api-docs/**").permitAll()
                        .pathMatchers("/webjars/**").permitAll()
                        .pathMatchers("/*/favicon.ico").permitAll()
                        .pathMatchers("/*/doc.html").permitAll()
                        .pathMatchers("/*/swagger-resources").permitAll()
                        .pathMatchers("/*/swagger-ui/**").permitAll()
                        .pathMatchers("/*/v3/api-docs/**").permitAll()
                        .pathMatchers("/*/webjars/**").permitAll()
                        // 2. 其他所有请求：需要认证，并使用自定义授权管理器
                        .anyExchange().authenticated()
                ).oauth2ResourceServer(oauth2 -> oauth2.
                        jwt(jwt -> {
                            try {
                                jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter)
                                   .jwtDecoder(jwtDecoder());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                );
            http.addFilterBefore(skipJwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION);
            // 添加日志过滤器
            http.addFilterAt(new WebFilter() {
                @Override
                public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                    log.debug("Request path: " + exchange.getRequest().getPath());
                    return chain.filter(exchange);
                }
            }, SecurityWebFiltersOrder.FIRST);
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        // 1. 从 classpath 读取 .pem 文件
        Resource resource = new ClassPathResource("keys/public-key.pem");
        String publicKeyPem = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        // 2. 去除 PEM 头尾和换行，提取 Base64 内容
        String publicKeyContent = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", ""); // 去除所有空白字符
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent)));

        // 3. 创建 NimbusReactiveJwtDecoder（使用 RS256 算法）
        return NimbusReactiveJwtDecoder.withPublicKey(pubKey).build();
    }


}
