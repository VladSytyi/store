package com.homework.store.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class CacheConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    private final Environment environment;

    public CacheConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public JedisPool jedisPool() {
        if (!environment.containsProperty("cache.host")) {
            log.warn("cache.host not found in properties, using default value localhost");
        }

        if (!environment.containsProperty("cache.port")) {
            log.warn("cache.port not found in properties, using default value 6379");
        }

        JedisPoolConfig defaultConfig = new JedisPoolConfig();
        return new JedisPool(
                defaultConfig,
                environment.getProperty("cache.host", "localhost"),
                environment.getProperty("cache.port", Integer.class, 6379));
    }


}
