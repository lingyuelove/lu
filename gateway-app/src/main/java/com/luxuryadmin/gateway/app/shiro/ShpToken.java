package com.luxuryadmin.gateway.app.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author monkey king
 * @date 2020-06-18 18:17:59
 */
public class ShpToken implements AuthenticationToken {

    private String token;

    public ShpToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
