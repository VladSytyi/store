package com.homework.store.repository;

import com.homework.store.PostgresTestContainer;
import com.homework.store.model.SearchCriteria;
import com.homework.store.model.Item;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class DefaultItemRepositoryTest {

    @Container
    public static PostgreSQLContainer<PostgresTestContainer> container = PostgresTestContainer.getInstance();

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management");


    private static DSLContext dslContext;
    private static final Integer pageSize = 5;

    private final ItemRepository itemRepository;

    public DefaultItemRepositoryTest() {
        this.itemRepository = new DefaultItemRepository(dslContext, pageSize);
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
        // pageSize is 5
        assertEquals(5, itemRepository.findAll(0).size());
    }

    @Test
    void findAllSecondPage() {
        // pageSize is 5
        assertEquals(1, itemRepository.findAll(1).size());
    }

    @Test
    void save() {
        Item item = new Item(null, "Smartphone", "Samsung", "mobile" ,"Electronics", BigDecimal.valueOf(111.11));
        Item saved = itemRepository.save(item);

        assertEquals(7, saved.id());
    }

    @Test
    void saveAll() {

        dslContext.execute("truncate table store.items");

        Item item1 = new Item(null, "Smartphone", "Samsung", "mobile" ,"Electronics", BigDecimal.valueOf(111.11));
        Item item2 = new Item(null, "Smartphone", "Samsung", "mobile" ,"Electronics", BigDecimal.valueOf(111.11));
        Item item3 = new Item(null, "Smartphone", "Samsung", "mobile" ,"Electronics", BigDecimal.valueOf(111.11));
        itemRepository.saveAll(List.of(item1, item2, item3));

        assertEquals(3, itemRepository.findAll(0).size());
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
        assertEquals(5, itemRepository.findAll(0).size());
    }

    @Test
    void findBySingleCriteria() {
        List<Item> items = itemRepository.findByCriteria(Map.of(SearchCriteria.NAME, "Laptop"));
        assertEquals(2, items.size());
    }

    @Test
    void findByMultipleCriteria() {
        List<Item> items = itemRepository.findByCriteria(Map.of(SearchCriteria.BRAND, "Apple", SearchCriteria.NAME, "Laptop"));
        assertEquals(2, items.size());
    }


    /// Test setup

    @BeforeAll
    static void beforeAll() {
        container.start();
        rabbitMQContainer.start();

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
        dslContext.execute("insert into store.items (name, brand, description, category, price) values ('Smartphone', 'LG', 'LG S22', 'Electronics', 55.66);");

    }

    @AfterAll
    static void afterAll() {
        container.stop();
        rabbitMQContainer.stop();
    }

    @DynamicPropertySource
    static void registerRabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }




}