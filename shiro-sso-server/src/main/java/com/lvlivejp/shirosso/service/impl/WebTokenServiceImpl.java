package com.lvlivejp.shirosso.service.impl;

import com.lvlivejp.shirosso.core.utils.JedisUtils;
import com.lvlivejp.shirosso.service.WebTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import static com.lvlivejp.shirosso.core.base.ShiroSsoConstant.*;

@Service
public class WebTokenServiceImpl implements WebTokenService {

    @Autowired
    private JedisUtils jedisUtils;
    @Value("${session.timeout}")
    private int SESSION_TIMEOUT;

    @Override
    public String generateToken(String sessionId) {
        String tokenKey = generateTokenKey();
        jedisUtils.set(getTokenKey(tokenKey),sessionId);
        return tokenKey;
    }

    public boolean checkToken(String token){
        String sessionId = jedisUtils.get(getTokenKey(token));
        if(!StringUtils.hasText(sessionId)){
            return false;
        }
        sessionId = getSessionKey(sessionId);
        if(!jedisUtils.exists(sessionId)){
            return false;
        }
        jedisUtils.expire(sessionId,SESSION_TIMEOUT);
        return true;
    };

    private String getTokenKey(String key){
        return SHIRO_TOKEN_PREFIX+key;
    }

    private String getSessionKey(String key){
        return SHIRO_SESSION_PREFIX+key;
    }

    private String generateTokenKey(){
        return UUID.randomUUID().toString();
    }
}
