package dev.tiltrikt.orion.api.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "orion.client")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrionClientConfigurationProperties {

    int leaseDurationSec = 90;
}