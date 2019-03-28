package com.lvlivejp.shirosso.core.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class JedisUtils {

    Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

    @Autowired
    private JedisPool jedisPool;

    public JedisUtils() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
    }

    private Jedis getResource() {
        return jedisPool.getResource();
    }

    private void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public void set(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(key, value);
        } finally {
            returnResource(jedis);
        }

    }

    public void set(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(key.getBytes(), serializer.serialize(value));
        } finally {
            returnResource(jedis);
        }

    }

    public void setex(byte[] key, byte[] value, int expireTime) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.setex(key, expireTime, value);
        } finally {
            returnResource(jedis);
        }

    }

    public void setex(String key, Object value, int expireTime) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.setex(key.getBytes(), expireTime, serializer.serialize(value));
        } finally {
            returnResource(jedis);
        }

    }

    public byte[] get(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.get(key);
        } finally {
            returnResource(jedis);
        }
    }

    public <T> T get(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return (T) serializer.deserialize(jedis.get(key.getBytes()));
        } catch (Exception e) {
            return null;
        } finally {
            returnResource(jedis);
        }
    }

    public void del(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.del(key);
        } finally {
            returnResource(jedis);
        }
    }

    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.del(key);
        } finally {
            returnResource(jedis);
        }
    }

    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.keys((shiro_session_prefix + "*").getBytes());
        } finally {
            returnResource(jedis);
        }
    }

    public Set<String> keysString(String shiro_session_prefix) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.keys(shiro_session_prefix + "*");
        } finally {
            returnResource(jedis);
        }
    }

}
