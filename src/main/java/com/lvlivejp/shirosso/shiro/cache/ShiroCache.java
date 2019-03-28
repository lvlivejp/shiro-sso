package com.lvlivejp.shirosso.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.Collection;
import java.util.Set;

public class ShiroCache implements Cache<String,Object> {
    @Override
    public Object get(String key) throws CacheException {
        return null;
    }

    @Override
    public Object put(String key, Object value) throws CacheException {
        return null;
    }

    @Override
    public Object remove(String key) throws CacheException {
        return null;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<String> keys() {
        return null;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }
}
