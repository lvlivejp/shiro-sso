package com.lvlivejp.shirosso.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisProperties properties;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        jedisPoolConfig.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        jedisPoolConfig.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());
        jedisPoolConfig.setMinIdle(properties.getJedis().getPool().getMinIdle());
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, properties.getHost(), properties.getPort(), ((Long) properties.getTimeout().toMillis()).intValue(), properties.getPassword(), properties.getDatabase());
        return jedisPool;
    }

}
