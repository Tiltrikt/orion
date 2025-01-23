package dev.tiltrikt.orion.client.kafka.autoconfiguration;

import dev.tiltrikt.orion.client.common.publisher.EventPublisher;
import dev.tiltrikt.orion.client.kafka.configuration.OrionKafkaConfigurationProperties;
import dev.tiltrikt.orion.client.kafka.factory.KafkaTemplateFactory;
import dev.tiltrikt.orion.client.kafka.publisher.KafkaEventPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@ConditionalOnDiscoveryEnabled
@ConditionalOnProperty(value = "orion.client.self-registration", matchIfMissing = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaPublisherAutoconfiguration {

    @NotNull OrionKafkaConfigurationProperties orionKafkaConfigurationProperties;

    private @NotNull Map<String, Object> kafkaTemplateConfigurationProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, orionKafkaConfigurationProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    @NotNull EventPublisher eventPublisher() {
        KafkaTemplateFactory kafkaTemplateFactory = new KafkaTemplateFactory();
        return new KafkaEventPublisher(
                kafkaTemplateFactory.create(kafkaTemplateConfigurationProperties()),
                kafkaTemplateFactory.create(kafkaTemplateConfigurationProperties()),
                kafkaTemplateFactory.create(kafkaTemplateConfigurationProperties())
        );
    }
}