package dev.tiltrikt.orion.service.kafka.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "orion.service")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrionServiceConfigurationProperties {

    int nodeId;

    int clusterSize = 1;
}
