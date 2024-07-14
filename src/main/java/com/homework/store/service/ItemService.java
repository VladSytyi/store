package com.homework.store.service;

import com.homework.store.api.models.ItemRequest;
import com.homework.store.model.Item;

import java.util.List;

public interface ItemService {

    Item findById(Long id);

    Item create(ItemRequest item);

    void createMultipleItems(List<ItemRequest> items);

    void deleteById(Long id);

    Item update(ItemRequest item);
}
