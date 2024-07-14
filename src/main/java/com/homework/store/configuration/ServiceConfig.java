package com.homework.store.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.store.service.DefaultItemService;
import com.homework.store.controller.filter.HttpTraceFilter;
import com.homework.store.service.ItemService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ServiceConfig {

    private final RepositoryConfig repositoryConfig;
    private final CacheConfig cacheConfig;
    private final RabbitTemplate rabbitTemplate;
    private final Environment environment;

    public ServiceConfig(RepositoryConfig repositoryConfig, CacheConfig cacheConfig, RabbitTemplate rabbitTemplate, Environment environment) {
        this.repositoryConfig = repositoryConfig;
        this.cacheConfig = cacheConfig;
        this.rabbitTemplate = rabbitTemplate;
        this.environment = environment;
    }

    @Bean
    public ItemService itemService() {
//        return new CacheableItemService(
//                new DefaultItemService(repositoryConfig.itemRepository()),
//                cacheConfig.jedis(),
//                new ObjectMapper()
//        );
        // TODO: Enable cache
        return new DefaultItemService(repositoryConfig.itemRepository());
    }

    @Bean
    public Queue httpTraceQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty("rabbit.queue")), true);
    }
}
