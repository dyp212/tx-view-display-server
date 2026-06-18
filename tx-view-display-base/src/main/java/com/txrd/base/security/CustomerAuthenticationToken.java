package com.txrd.base.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class CustomerAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;//代表“谁在认证”（如用户名、用户对象）。
    private Object credentials;//代表“如何证明身份”（如密码、JWT token）。

    // 用于认证成功后的构造器，需要传入权限列表
    public CustomerAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // 必须标记为已认证
    }

    // 用于认证过程中的构造器，不需要传入权限列表
    public CustomerAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(false); // 标记为未认证
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
