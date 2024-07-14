package com.homework.store.controller;

import com.homework.store.PostgresTestContainer;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemsControllerIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer<PostgresTestContainer> container = PostgresTestContainer.getInstance();

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        container.start();

        DataSource dataSource = new DriverManagerDataSource(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );

        DSLContext dslContext = DSL.using(dataSource, org.jooq.SQLDialect.POSTGRES);

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
    }

    @Test
    void createItem_validRequest_returnsCreatedItem() throws Exception {
        String itemRequestJson = "{\"name\":\"item1\",\"price\":100.0,\"brand\":\"brand1\",\"description\":\"description1\",\"category\":\"category1\"}";

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    void createMultipleItems_validRequest_returnsCreatedItems() throws Exception {
        String itemsRequestJson = "[{\"name\":\"item1\",\"price\":100.0,\"brand\":\"brand1\",\"description\":\"description1\",\"category\":\"category1\"}, {\"name\":\"item2\",\"price\":200.0,\"brand\":\"brand2\",\"description\":\"description2\",\"category\":\"category2\"}]";

        mockMvc.perform(post("/items/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemsRequestJson))
                .andExpect(status().isOk());

       //TODO: add request to validate newly create items
    }

    @Test
    void findItemById_validRequest_returnsItem() throws Exception {
        // caching is not available here

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.brand").value("Apple"))
                .andExpect(jsonPath("$.description").value("Apple Macbook Pro"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.price").value(1999.99));
    }



}