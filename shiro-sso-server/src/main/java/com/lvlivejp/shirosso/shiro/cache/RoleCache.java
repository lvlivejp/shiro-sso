package com.lvlivejp.shirosso.shiro.cache;

import com.lvlivejp.shirosso.core.utils.JedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.Set;

@Component
public class RoleCache<K, V> implements Cache<K, V> {

    @Autowired
    private JedisUtils jedisUtils;

    @Value("${session.timeout}")
    private int SESSION_TIMEOUT;

    private final String SHIRO_ROLE_PREFIX = "shiro_rolecache_id:";

    private byte[] getRedisKey(K k) {

        if (k instanceof String) {
            return (SHIRO_ROLE_PREFIX + k).getBytes();
        } else {
            return SerializationUtils.serialize(k);
        }


    }

    @Override
    public V get(K key) throws CacheException {
        return (V) SerializationUtils.deserialize(jedisUtils.get(getRedisKey(key)));
    }

    @Override
    public V put(K key, V value) throws CacheException {
        jedisUtils.setex(getRedisKey(key), SerializationUtils.serialize(value), SESSION_TIMEOUT);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        byte[] k = getRedisKey(key);
        V value = (V) SerializationUtils.deserialize(jedisUtils.get(k));
        jedisUtils.del(k);
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
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
