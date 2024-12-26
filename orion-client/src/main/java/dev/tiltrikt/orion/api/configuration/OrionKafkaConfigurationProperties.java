package dev.tiltrikt.orion.api.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "orion.client.kafka")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrionKafkaConfigurationProperties {

    @NotNull String bootstrapServers = "localhost:9092";
}
