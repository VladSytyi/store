package com.homework.store.repository;

import com.homework.store.model.Criteria;
import com.homework.store.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemRepository {

    List<Item> findAll(Integer page);
    Optional<Item> findById(Long id);

    List<Item> findByCriteria(Map<Criteria, String> searchParams);

    Item save(Item item);
    void saveAll(List<Item> items);

    Item update(Item item);

    void deleteById(Long id);
}
