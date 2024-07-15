package com.homework.store.service;

import com.homework.store.api.models.ItemRequest;
import com.homework.store.model.SearchCriteria;
import com.homework.store.model.Item;
import com.homework.store.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Item> findAll(Integer page) {

        return itemRepository.findAll(page);
    }

    @Override
    public List<Item> search(Map<SearchCriteria, String> searchParams) {
        searchParams.entrySet().removeIf(entry -> entry.getValue() == null);

        return itemRepository.findByCriteria(searchParams);
    }

    @Override
    public Item create(ItemRequest item) {
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
    public Item update(Long id, ItemRequest item) {
        // maybe receive model from controller , map it here / add validation / pass to repository

        return itemRepository.update(mapToItemWithId(id, item));
    }

    private Item mapToItem(ItemRequest itemRequest) {
        return new Item(null,
                itemRequest.getName(),
                itemRequest.getBrand(),
                itemRequest.getDescription(),
                itemRequest.getCategory(),
                itemRequest.getPrice());
    }

    private Item mapToItemWithId(Long id, ItemRequest itemRequest) {
        return new Item(id,
                itemRequest.getName(),
                itemRequest.getBrand(),
                itemRequest.getDescription(),
                itemRequest.getCategory(),
                itemRequest.getPrice());
    }
}
