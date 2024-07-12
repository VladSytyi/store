package com.homework.store.repository;

import com.homework.store.model.Item;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.generated.tables.Items;
import org.jooq.generated.tables.records.ItemsRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DefaultItemRepository implements ItemRepository {

    private final DSLContext dslContext;
    private final Integer pageSize;

    public DefaultItemRepository(DSLContext dslContext, Integer pageSize) {
        this.dslContext = dslContext;
        this.pageSize = pageSize;
    }

    @Override
    public List<Item> findAll() {
        return dslContext.select()
                .from(Items.ITEMS)
                .limit(pageSize)
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
    public void save(Item item) {
        dslContext.insertInto(Items.ITEMS)
                .set(buildItemRecord(item))
                .execute();
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
        return itemsRecord;
    }

    private Item toItem(Record record) {
        return new Item(record.get(Items.ITEMS.ID),
                record.get(Items.ITEMS.NAME),
                record.get(Items.ITEMS.PRICE),
                record.get(Items.ITEMS.DESCRIPTION));
    }
}
