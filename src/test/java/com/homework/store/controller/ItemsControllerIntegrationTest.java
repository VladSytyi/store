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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        mockMvc.perform(get("/items/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("item1"))
                .andExpect(jsonPath("$.brand").value("brand1"));

        mockMvc.perform(get("/items/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.name").value("item2"))
                .andExpect(jsonPath("$.brand").value("brand2"));
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

    @Test
    void findByCriteria_validRequest_returnsItems() throws Exception {
        mockMvc.perform(get("/items/search?name=Smartphone&brand=Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Smartphone"))
                .andExpect(jsonPath("$[0].brand").value("Samsung"))
                .andExpect(jsonPath("$[0].description").value("Samsung Galaxy S21"))
                .andExpect(jsonPath("$[0].category").value("Electronics"));
    }


    @Test
    void deleteItem_validRequest_returnsOk_and_item_not_found() throws Exception {
        // delete item
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk());

        // try to get deleted item
        mockMvc.perform(get("/items/1"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void updateItem_validRequest_returnsUpdatedItem() throws Exception {
        String itemRequestJson = "{\"name\":\"item1\",\"price\":100.0,\"brand\":\"brand1\",\"description\":\"description1\",\"category\":\"category1\"}";

        // update item
        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemRequestJson))
                .andExpect(status().isOk());

        // get updated item
        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("item1"))
                .andExpect(jsonPath("$.brand").value("brand1"))
                .andExpect(jsonPath("$.description").value("description1"))
                .andExpect(jsonPath("$.category").value("category1"))
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    void findAll_with_paging_works() throws Exception {
        mockMvc.perform(get("/items?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));

        mockMvc.perform(get("/items?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}