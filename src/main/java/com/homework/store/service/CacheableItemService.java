package com.homework.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.store.api.models.ItemRequest;
import com.homework.store.model.Criteria;
import com.homework.store.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CacheableItemService implements ItemService {

    private static final Logger log = LoggerFactory.getLogger(CacheableItemService.class);
    private static final String ITEM_PREFIX = "item:";
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

        String itemJson = jedis.get(ITEM_PREFIX + id);

        if (itemJson != null) {
            return objectMapper.convertValue(itemJson, Item.class);
        }

        Item item = defaultItemService.findById(id);

        try {
            jedis.set(ITEM_PREFIX + id, objectMapper.writeValueAsString(item));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return item;

    }

    @Override
    public List<Item> findAll(Integer page) {
        return defaultItemService.findAll(page);
    }

    @Override
    public List<Item> search(Map<Criteria, String> searchParams) {
        return defaultItemService.search(searchParams);
    }

    @Override
    public Item create(ItemRequest item) {
        Item savedItem = defaultItemService.create(item);

        try {
            jedis.set(ITEM_PREFIX + savedItem.id(), objectMapper.writeValueAsString(savedItem));
        } catch (IOException e) {
            log.error("Error while saving item to cache", e);
        }
        return savedItem;
    }

    @Override
    public void createMultipleItems(List<ItemRequest> items) {
        // TODO: do we need to cache all items?
        defaultItemService.createMultipleItems(items);
    }

    @Override
    public void deleteById(Long id) {
        // we need to improve this because we are no aware if the item was deleted from the database
        defaultItemService.deleteById(id);

        try {
            jedis.del(ITEM_PREFIX + id);
        } catch (Exception e) {
            log.error("Error while deleting item from cache", e);
        }
    }

    @Override
    public Item update(Long id, ItemRequest item) {
        // Todo: improve
        Item updatedItem = defaultItemService.update(id ,item);
        try {
            jedis.set(ITEM_PREFIX + updatedItem.id(), objectMapper.writeValueAsString(updatedItem));
        } catch (IOException e) {
            log.error("Error while updating item in cache", e);
        }
        return updatedItem;
    }
}
