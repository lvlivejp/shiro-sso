package com.lvlivejp.shirosso.shiro.authc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

@Data
@AllArgsConstructor
public class ShiroSsoToken implements AuthenticationToken {

    private String token;
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
