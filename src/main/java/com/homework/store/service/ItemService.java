package com.homework.store.service;

import com.homework.store.api.models.ItemRequest;
import com.homework.store.model.SearchCriteria;
import com.homework.store.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {

    Item findById(Long id);

    List<Item> findAll(Integer page);

    List<Item> search(Map<SearchCriteria, String> searchParams);

    Item create(ItemRequest item);

    void createMultipleItems(List<ItemRequest> items);

    void deleteById(Long id);

    Item update(Long id, ItemRequest item);
}
