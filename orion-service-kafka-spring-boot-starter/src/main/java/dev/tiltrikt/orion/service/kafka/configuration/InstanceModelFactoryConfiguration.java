package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.service.domain.model.factory.InstanceModelFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstanceModelFactoryConfiguration {

    @Bean
    @NotNull InstanceModelFactory instanceModelFactory() {
        return new InstanceModelFactory();
    }
}
