package com.homework.store.controller;


import com.homework.store.api.controllers.ItemsApi;
import com.homework.store.api.models.ItemRequest;
import com.homework.store.api.models.ItemResponse;
import com.homework.store.model.Item;
import com.homework.store.service.ItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemsController implements ItemsApi {

    private static final Logger log = LoggerFactory.getLogger(ItemsController.class);

    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public ResponseEntity<ItemResponse> createItem(ItemRequest item) {

        try {
            ItemResponse itemResponse = mapToResponse(itemService.create(item));

            return ResponseEntity.ok(itemResponse);

        } catch (Exception e) {
            log.error("Error while creating item", e);
            return ResponseEntity.status(500).build();
        }

    }

    @Override
    public ResponseEntity<Void> createMultipleItems(List<@Valid ItemRequest> item) {

        try {
            itemService.createMultipleItems(item);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error while creating items", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteItem(Long itemId) {

        try {
            itemService.deleteById(itemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error while deleting item", e);
            return ResponseEntity.status(500).build();
        }

    }

    @Override
    public ResponseEntity<List<ItemResponse>> getAllItems(Integer page) {

        try {
            List<Item> items = itemService.findAll(page);
            return ResponseEntity.ok(items.stream().map(this::mapToResponse).toList());
        } catch (Exception e) {
            log.error("Error while getting items", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<ItemResponse> getItemById(Long itemId) {

        try {
            Item item = itemService.findById(itemId);
            return ResponseEntity.ok(mapToResponse(item));
        } catch (Exception e) {
            log.error("Error while getting item", e);
            return ResponseEntity.status(500).build();
        }

    }

    @Override
    public ResponseEntity<List<ItemResponse>> searchItems(Long id, String name, String brand) {
        return ItemsApi.super.searchItems(id, name, brand);
    }


    @Override
    public ResponseEntity<ItemResponse> updateItem(Long itemId, ItemRequest item) {
        try {
            Item updatedItem = itemService.update(itemId, item);
            return ResponseEntity.ok(mapToResponse(updatedItem));

        } catch (Exception e) {
            log.error("Error while updating item", e);
            return ResponseEntity.status(500).build();
        }
    }

    private ItemResponse mapToResponse(Item item) {
        return new ItemResponse(item.id(), item.name(), item.price(), item.brand(), item.description(), item.category());
    }
}
