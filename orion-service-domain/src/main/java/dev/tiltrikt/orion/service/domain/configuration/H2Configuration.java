package dev.tiltrikt.orion.service.domain.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@EntityScan(basePackages = "dev.tiltrikt.orion.service.domain.model")
@EnableJpaRepositories(basePackages = "dev.tiltrikt.orion.service.domain.repository")
public class H2Configuration {

    @Bean
    @NotNull DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/orion");
        dataSource.setUsername("dev");
        dataSource.setPassword("dev");
        return dataSource;
    }
}
