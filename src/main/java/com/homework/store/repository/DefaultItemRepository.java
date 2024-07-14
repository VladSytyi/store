package com.homework.store.repository;

import com.homework.store.model.Item;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.generated.tables.Items;
import org.jooq.generated.tables.records.ItemsRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DefaultItemRepository implements ItemRepository {

    private final DSLContext dslContext;
    private final int pageSize;

    public DefaultItemRepository(DSLContext dslContext, @Value("${repo.page.size}") int pageSize) {
        this.dslContext = dslContext;
        this.pageSize = pageSize;
    }

    @Override
    public List<Item> findAll(Integer page) {
        int pageNumber = page * pageSize;

        return dslContext.select()
                .from(Items.ITEMS)
                .limit(pageSize)
                .offset(pageNumber)
                .fetch()
                .map(this::toItem);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return dslContext.select()
                .from(Items.ITEMS)
                .where(Items.ITEMS.ID.eq(id))
                .fetchOptional()
                .map(this::toItem);
    }

    @Override
    public Item save(Item item) {
        return dslContext.insertInto(Items.ITEMS)
                .set(buildItemRecord(item))
                .returning()
                .fetchOptional()
                .map(this::toItem)
                .orElseThrow(() -> new RuntimeException("failed to save item"));
    }


    @Override
    public void deleteById(Long id) {
        dslContext.deleteFrom(Items.ITEMS)
                .where(Items.ITEMS.ID.eq(id))
                .execute();
    }

    @Override
    public void saveAll(List<Item> items) {
        dslContext.batchInsert(
                        items.stream()
                                .map(this::buildItemRecord)
                                .toList())
                .execute();
    }

    @Override
    public Item update(Item item) {
        return dslContext.update(Items.ITEMS)
                .set(Items.ITEMS.NAME, item.name())
                .set(Items.ITEMS.PRICE, item.price())
                .set(Items.ITEMS.DESCRIPTION, item.description())
                .set(Items.ITEMS.BRAND, item.brand())
                .set(Items.ITEMS.CATEGORY, item.category())
                .where(Items.ITEMS.ID.eq(item.id()))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("failed to update item"))
                .map(this::toItem);
    }

    private ItemsRecord buildItemRecord(Item item) {
        ItemsRecord itemsRecord = dslContext.newRecord(Items.ITEMS, item);
        itemsRecord.set(Items.ITEMS.NAME, item.name());
        itemsRecord.set(Items.ITEMS.PRICE, item.price());
        itemsRecord.set(Items.ITEMS.DESCRIPTION, item.description());
        itemsRecord.set(Items.ITEMS.BRAND, item.brand());
        itemsRecord.set(Items.ITEMS.CATEGORY, item.category());
        return itemsRecord;
    }

    private Item toItem(Record record) {
        return new Item(record.get(Items.ITEMS.ID),
                record.get(Items.ITEMS.NAME),
                record.get(Items.ITEMS.BRAND),
                record.get(Items.ITEMS.DESCRIPTION),
                record.get(Items.ITEMS.CATEGORY),
                record.get(Items.ITEMS.PRICE)
                );
    }
}
