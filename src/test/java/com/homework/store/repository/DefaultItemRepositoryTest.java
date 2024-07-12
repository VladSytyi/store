package com.homework.store.repository;

import com.homework.store.PostgresTestContainer;
import com.homework.store.model.Item;
import org.jooq.CloseableDSLContext;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.generated.tables.Items;
import org.jooq.impl.DSL;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DefaultItemRepositoryTest {

    @ClassRule
    public static PostgreSQLContainer<PostgresTestContainer> container = PostgresTestContainer.getInstance();

    private static DSLContext dslContext;

    private final DefaultItemRepository itemRepository;

    public DefaultItemRepositoryTest() {
        this.itemRepository = new DefaultItemRepository(dslContext);
    }

    @BeforeAll
    static void beforeAll() {
        container.start();

        DataSource dataSource = new DriverManagerDataSource(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );

        dslContext = DSL.using(dataSource, org.jooq.SQLDialect.POSTGRES);

        dslContext.execute("drop schema if exists store cascade");
        dslContext.execute("create schema if not exists store");
        dslContext.execute("drop table if exists store.items");
        dslContext.execute("""
                create table store.items (
                  id bigserial primary key,
                  name varchar(100) not null,
                  brand varchar(100),
                  description varchar(255),
                  category varchar(100) not null,
                  price decimal(10, 2) not null
                )""");

        dslContext.execute("insert into store.items (name, brand, description, category, price) values ('Laptop', 'Apple', 'Apple Macbook Pro', 'Electronics', 1999.99);");
        dslContext.execute("insert into store.items (name, brand, description, category, price) values ('Smartphone', 'Samsung', 'Samsung Galaxy S21', 'Electronics', 999.99);");
        dslContext.execute("insert into store.items (name, brand, description, category, price) values ('Smartwatch', 'Apple', 'Apple Watch Series 7', 'Electronics', 399.99);");
        dslContext.execute("insert into store.items (name, brand, description, category, price) values ('Tablet', 'Apple', 'Apple iPad Pro', 'Electronics', 799.99);");
        dslContext.execute("insert into store.items (name, brand, description, category, price) values ('Laptop', 'Apple', 'Apple Macbook Pro', 'Electronics', 1999.99);");

    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }


    @Test
    void findById() {

        itemRepository.findById(1L).ifPresentOrElse(
            item -> {
                assertEquals("Laptop", item.name());
                assertEquals("Apple", item.brand());
            },
            () -> fail("Item not found")
        );
    }

    @Test
    void findAll() {
        assertEquals(5, itemRepository.findAll().size());
    }

    @Test
    void save() {
        Item item = new Item(null, "Smartphone", "Samsung", "mobile" ,"Electronics", BigDecimal.valueOf(111.11));
        itemRepository.save(item);

        assertEquals(6, itemRepository.findById(6L).get().id());
    }

    @Test
    void saveAll() {

        dslContext.execute("truncate table store.items");

        Item item1 = new Item(null, "Smartphone", "Samsung", "mobile" ,"Electronics", BigDecimal.valueOf(111.11));
        Item item2 = new Item(null, "Smartphone", "Samsung", "mobile" ,"Electronics", BigDecimal.valueOf(111.11));
        Item item3 = new Item(null, "Smartphone", "Samsung", "mobile" ,"Electronics", BigDecimal.valueOf(111.11));
        itemRepository.saveAll(List.of(item1, item2, item3));

        assertEquals(3, itemRepository.findAll().size());
    }

    @Test
    void update() {
        Item item = itemRepository.findById(1L).get();
        BigDecimal newPrice = BigDecimal.valueOf(1111.11);
        Item updated = new Item(item.id(), item.name(), item.brand(), item.description() ,item.category(), newPrice);

        Item fromDb = itemRepository.update(updated);

        assertEquals(BigDecimal.valueOf(1111.11), fromDb.price());
    }

    @Test
    void deleteById() {
        itemRepository.deleteById(1L);
        assertEquals(4, itemRepository.findAll().size());
    }





}