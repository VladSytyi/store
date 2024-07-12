package com.homework.store.service;

import com.homework.store.model.Item;
import com.homework.store.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultItemService implements ItemService {

    private static final Logger log = LoggerFactory.getLogger(DefaultItemService.class);

    private final ItemRepository itemRepository;

    public DefaultItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Override
    public Item save(Item item) {
        // add validation
        return itemRepository.save(item);
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Item update(Item item) {
        // maybe receive model from controller , map it here / add validation / pass to repository

        return itemRepository.update(item);
    }
}