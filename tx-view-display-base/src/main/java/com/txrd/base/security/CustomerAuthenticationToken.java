package com.txrd.base.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Getter
@Setter
public class CustomerAuthenticationToken extends JwtAuthenticationToken {
//    private final Object principal;//代表“谁在认证”（如用户名、用户对象）。
//    private Object credentials;//代表“如何证明身份”（如密码、JWT token）。
    private String userId;
    private String account;

    // 用于认证成功后的构造器，需要传入权限列表
    public CustomerAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String userId, String account) {
        super(jwt, authorities, userId);
        this.userId = userId;
        this.account = account;
        super.setAuthenticated(true); // 必须标记为已认证
    }

    // 用于认证过程中的构造器，不需要传入权限列表
//    public CustomerAuthenticationToken(Object principal, Object credentials) {
//        super(null);
//        this.principal = principal;
//        this.credentials = credentials;
//        super.setAuthenticated(false); // 标记为未认证
//    }
//
//    @Override
//    public Object getCredentials() {
//        return this.credentials;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return this.principal;
//    }
}
