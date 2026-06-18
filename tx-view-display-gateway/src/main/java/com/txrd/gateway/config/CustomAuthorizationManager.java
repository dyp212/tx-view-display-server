package com.txrd.gateway.config;

import com.txrd.gateway.service.PermissionCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final PermissionCacheService permissionCacheService;

    public CustomAuthorizationManager(PermissionCacheService permissionCacheService) {
        this.permissionCacheService = permissionCacheService;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        ServerHttpRequest request = context.getExchange().getRequest();
        String path = request.getPath().value();

        // 1. 获取该路径所需的权限标识
        String requiredPermission = permissionCacheService.getRequiredPermission(path);

        // 2. 如果该路径不需要特定权限（公开或无需鉴权），直接通过
        if (requiredPermission == null || requiredPermission.isEmpty()) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 3. 获取当前用户的权限列表
        return authentication.map(auth -> {
            if (auth == null || !auth.isAuthenticated()) {
                return new AuthorizationDecision(false);
            }

            // 从 Authentication 中获取已解析的 Authorities
            // 注意：如果在 JwtConverter 里没放 permissions，这里可能需要重新查
            List<String> userPermissions = auth.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.toList());

            // 4. 校验逻辑：用户是否包含所需权限
            boolean hasPermission = userPermissions.contains(requiredPermission);

            if (!hasPermission) {
                log.warn("用户 {} 访问 {} 失败，缺少权限: {}", auth.getName(), path, requiredPermission);
            }

            return new AuthorizationDecision(hasPermission);
        });
    }
}
