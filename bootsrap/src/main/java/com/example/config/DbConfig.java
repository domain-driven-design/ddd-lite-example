package com.example.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class DbConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean(name = "businessDataSource")
    @Primary
    public DataSource businessDataSource() {
        return DataSourceBuilder.create().build();
    }

    @PostConstruct
    public void migrate() {
        Flyway.configure()
                .dataSource(businessDataSource())
                .load()
                .migrate();
    }
}
