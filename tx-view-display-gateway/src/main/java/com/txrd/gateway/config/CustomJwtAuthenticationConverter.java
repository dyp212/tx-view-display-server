package com.txrd.gateway.config;

import com.txrd.base.security.CustomerAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Mono<CustomerAuthenticationToken>> {

    @Override
    public Mono<CustomerAuthenticationToken> convert(Jwt jwt) {
        // 1. 提取主体 (Subject)
        String userId = jwt.getSubject();

        // 2. 提取自定义 Claim: username
        String account = jwt.getClaimAsString("username");

        // 3. 提取自定义 Claim: permissions
        // 注意：如果 JWT 里没有存 permissions，这里可以留空，后续在 AuthorizationManager 里查
        List<String> permissions = jwt.getClaimAsStringList("permissions");

        // 4. 将 permissions 转换为 Spring Security 的 GrantedAuthority
        Collection<GrantedAuthority> authorities = Collections.emptyList();
        if (permissions != null) {
            authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        // 5. 构建 MOno
        return Mono.just(new CustomerAuthenticationToken(userId, jwt, authorities));
    }
}
