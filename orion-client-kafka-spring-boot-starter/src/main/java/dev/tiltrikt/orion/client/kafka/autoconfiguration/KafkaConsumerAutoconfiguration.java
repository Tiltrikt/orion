package dev.tiltrikt.orion.client.kafka.autoconfiguration;

import dev.tiltrikt.orion.client.kafka.configuration.OrionKafkaConfigurationProperties;
import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@ConditionalOnDiscoveryEnabled
@ConditionalOnProperty(value = "orion.client.fetch-registry", matchIfMissing = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerAutoconfiguration {

    @NotNull OrionKafkaConfigurationProperties orionKafkaConfigurationProperties;

    @Bean
    @NotNull ConsumerFactory<String, RegistryUpdateEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(RegistryUpdateEvent.class));
    }

    @Bean
    @NotNull ConcurrentKafkaListenerContainerFactory<String, RegistryUpdateEvent> kafkaListenerContainerFactory(
            @NotNull ConsumerFactory<String, RegistryUpdateEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, RegistryUpdateEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    private @NotNull Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, orionKafkaConfigurationProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, orionKafkaConfigurationProperties.getTrustedPackages());
        return props;
    }
}
