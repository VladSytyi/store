package com.homework.store.repository;

import com.homework.store.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> findAll();
    Optional<Item> findById(Long id);

    Item save(Item item);
    void saveAll(List<Item> items);

    Item update(Item item);

    void deleteById(Long id);
}
