package com.homework.store.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.store.service.CacheableItemService;
import com.homework.store.service.DefaultItemService;
import com.homework.store.service.ItemService;
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
        return new CacheableItemService(
                new DefaultItemService(repositoryConfig.itemRepository()),
                cacheConfig.jedis(),
                new ObjectMapper()
        );
    }
}
