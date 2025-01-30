package dev.tiltrikt.orion.service.kafka.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "dev.tiltrikt.orion.service.domain.model")
@EnableJpaRepositories(basePackages = "dev.tiltrikt.orion.service.domain.repository")
public class RepositoryConfiguration {

}
