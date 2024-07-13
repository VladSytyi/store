package com.homework.store.controller;


import com.github.dockerjava.api.exception.NotFoundException;
import com.homework.store.api.controllers.ItemsApi;
import com.homework.store.api.models.Item;
import com.homework.store.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemsController implements ItemsApi {

    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public ResponseEntity<Item> createItem(Item item) {
        return ItemsApi.super.createItem(item);
    }

    @Override
    public ResponseEntity<List<Item>> createMultipleItems(List<@Valid Item> item) {
        return ItemsApi.super.createMultipleItems(item);
    }

    @Override
    public ResponseEntity<Void> deleteItem(String itemId) {
        return ItemsApi.super.deleteItem(itemId);
    }

    @Override
    public ResponseEntity<List<Item>> getAllItems(Integer limit) {
        return ItemsApi.super.getAllItems(limit);
    }

    @Override
    public ResponseEntity<Item> getItemById(String itemId) {
        return ItemsApi.super.getItemById(itemId);
    }

    @Override
    public ResponseEntity<List<Item>> searchItems(String name, String anotherField) {
        return ItemsApi.super.searchItems(name, anotherField);
    }

    @Override
    public ResponseEntity<Item> updateItem(String itemId, Item item) {
        return ItemsApi.super.updateItem(itemId, item);
    }
}
