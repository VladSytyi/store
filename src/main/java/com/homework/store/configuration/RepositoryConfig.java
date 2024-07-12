package com.homework.store.configuration;

import com.homework.store.repository.DefaultItemRepository;
import com.homework.store.repository.ItemRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class RepositoryConfig {

    private final Environment environment;

    private static final Logger log = LoggerFactory.getLogger(RepositoryConfig.class);

    public RepositoryConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DSLContext dslContext(DataSource dataSource) {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("jdbc.driver")));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.username"));
        dataSource.setPassword(environment.getProperty("jdbc.password"));
        return dataSource;
    }

    @Bean
    public ItemRepository itemRepository() {
        if (!environment.containsProperty("repo.page.size")) {
            log.warn("repo.page.size not found in properties, using default value 10");
        }

        return new DefaultItemRepository(dslContext(dataSource()), environment.getProperty("repo.page.size", Integer.class, 10));
    }
}
