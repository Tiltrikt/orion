package dev.tiltrikt.orion.client.kafka.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "orion.client.kafka")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrionKafkaConfigurationProperties {

    @NotNull List<String> bootstrapServers = Collections.singletonList("localhost:9092");

    @NotNull String trustedPackages = "dev.tiltrikt.orion.common.event";
}
