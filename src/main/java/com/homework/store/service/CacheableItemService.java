package com.homework.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.store.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;

@Service
public class CacheableItemService implements ItemService {

    private static final Logger log = LoggerFactory.getLogger(CacheableItemService.class);
    private final ItemService defaultItemService;
    private final JedisPooled jedis;
    private final ObjectMapper objectMapper;

    public CacheableItemService(ItemService defaultItemService, JedisPooled jedis, ObjectMapper objectMapper) {
        this.defaultItemService = defaultItemService;
        this.jedis = jedis;
        this.objectMapper = objectMapper;
    }

    @Override
    public Item findById(Long id) {

        String itemJson = jedis.get("item:" + id);

        if (itemJson != null) {
            return objectMapper.convertValue(itemJson, Item.class);
        }

        Item item = defaultItemService.findById(id);

        try {
            jedis.set("item:" + id, objectMapper.writeValueAsString(item));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return item;

    }

    @Override
    public Item save(Item item) {
        Item savedItem = defaultItemService.save(item);

        try {
            jedis.set("item:" + savedItem.id(), objectMapper.writeValueAsString(savedItem));
        } catch (IOException e) {
            log.error("Error while saving item to cache", e);
        }
        return savedItem;
    }

    @Override
    public void deleteById(Long id) {
        // we need to improve this because we are no aware if the item was deleted from the database
        defaultItemService.deleteById(id);

        try {
            jedis.del("item:" + id);
        } catch (Exception e) {
            log.error("Error while deleting item from cache", e);
        }
    }

    @Override
    public Item update(Item item) {
        Item updatedItem = defaultItemService.update(item);
        try {
            jedis.set("item:" + updatedItem.id(), objectMapper.writeValueAsString(updatedItem));
        } catch (IOException e) {
            log.error("Error while updating item in cache", e);
        }
        return updatedItem;
    }
}
