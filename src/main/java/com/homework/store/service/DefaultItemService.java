package com.homework.store.service;

import com.homework.store.api.models.ItemRequest;
import com.homework.store.model.Item;
import com.homework.store.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Item create(ItemRequest item) {
        // add validation

        return itemRepository.save(mapToItem(item));
    }

    @Override
    public void createMultipleItems(List<ItemRequest> items) {

        List<Item> data = items.stream().map(this::mapToItem).collect(Collectors.toList());

        itemRepository.saveAll(data);
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Item update(ItemRequest item) {
        // maybe receive model from controller , map it here / add validation / pass to repository

        return itemRepository.update(null);
    }

    private Item mapToItem(ItemRequest itemRequest) {
        return new Item(null,
                itemRequest.getName(),
                itemRequest.getBrand(),
                itemRequest.getDescription(),
                itemRequest.getCategory(),
                itemRequest.getPrice());
    }
}
