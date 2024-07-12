package com.homework.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.store.model.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class CacheableItemServiceTest {

  //  private final CacheableItemService cacheableItemService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void conversionTest() {
        Item item = new Item(1L, "test", "br", "test", "dasd", BigDecimal.ONE);

        String json = objectMapper.convertValue(item, String.class);

        Item convertedItem = objectMapper.convertValue(json, Item.class);

        Assertions.assertEquals(item, convertedItem);
    }

}