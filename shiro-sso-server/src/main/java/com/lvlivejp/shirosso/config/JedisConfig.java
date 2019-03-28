package com.lvlivejp.shirosso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class JedisConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisProperties properties;


    //单节点redis
    @Bean
    public JedisPool redisNoClusterPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        jedisPoolConfig.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        jedisPoolConfig.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());
        jedisPoolConfig.setMinIdle(properties.getJedis().getPool().getMinIdle());
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, properties.getHost(), properties.getPort(), ((Long) properties.getTimeout().toMillis()).intValue(), properties.getPassword(), properties.getDatabase());
        return jedisPool;
    }

    //redis集群
    @Bean
    public JedisCluster redisClusterPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        jedisPoolConfig.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        jedisPoolConfig.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());
        jedisPoolConfig.setMinIdle(properties.getJedis().getPool().getMinIdle());
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : properties.getCluster().getNodes()) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        int timeout = ((Long) properties.getTimeout().toMillis()).intValue();
        return new JedisCluster(nodes,timeout,timeout,3,jedisPoolConfig);
    }

}
