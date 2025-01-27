package dev.tiltrikt.orion.service.domain.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {
        "dev.tiltrikt.orion.service.domain.model",
})
@EnableJpaRepositories(basePackages = {
        "dev.tiltrikt.orion.service.domain.repository",
})
public class H2Configuration {

//    @Bean
//    @NotNull DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:orion");
//        dataSource.setUsername("dev");
//        dataSource.setPassword("dev");
//        return dataSource;
//    }
}
