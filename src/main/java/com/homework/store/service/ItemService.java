package com.homework.store.service;

import com.homework.store.model.Item;

public interface ItemService {

    Item findById(Long id);

    Item save(Item item);

    void deleteById(Long id);

    Item update(Item item);
}
