package com.lvlivejp.shirosso.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroCacheManager implements CacheManager {

    @Autowired
    private AuthorizationRedisCache authorizationRedisCache;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return (Cache<K, V>) authorizationRedisCache;
    }
}
