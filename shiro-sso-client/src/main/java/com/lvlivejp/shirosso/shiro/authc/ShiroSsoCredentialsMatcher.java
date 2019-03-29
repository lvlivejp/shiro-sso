package com.lvlivejp.shirosso.shiro.authc;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ShiroSsoCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        RestTemplate restT = new RestTemplate();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("token", token.getPrincipal());
        ResponseEntity<String> stringResponseEntity = restT.postForEntity("http://127.0.0.1:9090/auth/checkToken", paramMap, String.class);
        return stringResponseEntity.getStatusCode().is2xxSuccessful();
    }
}
