package com.lvlivejp.shirosso.shiro.authc;

import com.lvlivejp.shirosso.config.properties.ShiroProperties;
import com.lvlivejp.shirosso.core.base.ShiroSsoConstant;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ShiroSsoCredentialsMatcher extends SimpleCredentialsMatcher {
    @Autowired
    private ShiroProperties shiroProperties;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        RestTemplate restT = new RestTemplate();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add(ShiroSsoConstant.SHIRO_SSO_CLIENT_TOKEN, token.getPrincipal());
        ResponseEntity<String> stringResponseEntity = null;
        try {
            stringResponseEntity = restT.postForEntity(shiroProperties.getSsoServerTokenCheckUrl(), paramMap, String.class);
        } catch (RestClientException e) {
            return false;
        }
        return stringResponseEntity.getStatusCode().is2xxSuccessful();
    }
}
