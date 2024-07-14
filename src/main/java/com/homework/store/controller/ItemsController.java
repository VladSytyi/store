package com.homework.store.controller;


import com.homework.store.api.controllers.ItemsApi;
import com.homework.store.api.models.ItemRequest;
import com.homework.store.api.models.ItemResponse;
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
    public ResponseEntity<ItemResponse> createItem(ItemRequest item) {

      //  itemService.save()
     //   itemService.create(item);

        return ItemsApi.super.createItem(item);
    }

    @Override
    public ResponseEntity<List<ItemResponse>> createMultipleItems(List<@Valid ItemRequest> item) {
        return ItemsApi.super.createMultipleItems(item);
    }

    @Override
    public ResponseEntity<Void> deleteItem(String itemId) {
        return ItemsApi.super.deleteItem(itemId);
    }

    @Override
    public ResponseEntity<List<ItemResponse>> getAllItems(Integer limit) {
        return ItemsApi.super.getAllItems(limit);
    }

    @Override
    public ResponseEntity<ItemResponse> getItemById(String itemId) {
        return ItemsApi.super.getItemById(itemId);
    }

    @Override
    public ResponseEntity<List<ItemResponse>> searchItems(Long id, String name, String brand) {
        return ItemsApi.super.searchItems(id, name, brand);
    }


    @Override
    public ResponseEntity<ItemResponse> updateItem(String itemId, ItemRequest item) {
        return ItemsApi.super.updateItem(itemId, item);
    }
}
