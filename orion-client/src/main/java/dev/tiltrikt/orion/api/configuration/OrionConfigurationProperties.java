package dev.tiltrikt.orion.api.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "orion")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrionConfigurationProperties {

    @NotNull String kafkaBootstrapServer = "localhost:9092";

    @NestedConfigurationProperty
    @NotNull OrionClientConfigurationProperties client;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OrionClientConfigurationProperties {

        boolean fetchRegistry = true;

        boolean selfRegistration = true;

        int heartbeatRateSec = 30;

        int leaseDurationSec = 90;
    }
}