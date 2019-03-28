package com.lvlivejp.shirosso.shiro.cache;

import com.lvlivejp.shirosso.core.utils.JedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class AuthorizationRedisCache implements Cache<SimplePrincipalCollection,Object> {

    @Autowired
    private JedisUtils jedisUtils;

    @Value("${session.timeout}")
    private int SESSION_TIMEOUT;

    private final String SHIRO_ROLE_PREFIX = "shiro_cache_id:";

    private String getRedisKey(SimplePrincipalCollection k) {
        return (SHIRO_ROLE_PREFIX + k);
    }

    @Override
    public Object get(SimplePrincipalCollection key) throws CacheException {
        return jedisUtils.get(getRedisKey(key));
    }

    @Override
    public Object put(SimplePrincipalCollection key, Object value) throws CacheException {
        jedisUtils.setex(getRedisKey(key), value, SESSION_TIMEOUT);
        return value;
    }

    @Override
    public Object remove(SimplePrincipalCollection key) throws CacheException {
        Object value = jedisUtils.get(getRedisKey(key));
        jedisUtils.del(getRedisKey(key));
        return value;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<SimplePrincipalCollection> keys() {
        return null;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }
}
