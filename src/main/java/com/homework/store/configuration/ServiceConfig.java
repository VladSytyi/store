package com.homework.store.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.store.service.DefaultItemService;
import com.homework.store.service.HttpTraceFilter;
import com.homework.store.service.ItemService;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ServiceConfig {

    private final RepositoryConfig repositoryConfig;
    private final CacheConfig cacheConfig;

    public ServiceConfig(RepositoryConfig repositoryConfig, CacheConfig cacheConfig) {
        this.repositoryConfig = repositoryConfig;
        this.cacheConfig = cacheConfig;
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
        return new Queue("http-trace", true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate();
    }

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    // TODO: Enable http trace
//    @Bean
//    public HttpTraceFilter traceFilter() {
//        return new  HttpTraceFilter(rabbitTemplate(), mapper());
//    }
}
