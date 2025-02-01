package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrionServiceNodeConfiguration {

    @Bean
    @NotNull OrionServiceNode nodeModel(@NotNull OrionServiceConfigurationProperties properties) {
        return new OrionServiceNode(properties.getNodeId(), false, 1);
    }
}
