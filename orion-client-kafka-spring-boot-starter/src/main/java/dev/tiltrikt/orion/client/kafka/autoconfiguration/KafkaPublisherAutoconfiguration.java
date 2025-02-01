package dev.tiltrikt.orion.client.kafka.autoconfiguration;

import dev.tiltrikt.orion.client.common.publisher.EventPublisher;
import dev.tiltrikt.orion.client.domain.model.OrionInstance;
import dev.tiltrikt.orion.client.kafka.configuration.OrionKafkaConfigurationProperties;
import dev.tiltrikt.orion.client.kafka.consumer.HeartbeatErrorHandler;
import dev.tiltrikt.orion.client.kafka.factory.KafkaTemplateFactory;
import dev.tiltrikt.orion.client.kafka.publisher.KafkaEventPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
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

    @Bean
    @NotNull ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new StringDeserializer());
    }

    @Bean
    @NotNull ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            @NotNull ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    @NotNull HeartbeatErrorHandler heartbeatErrorHandler(
            @NotNull OrionInstance thisOrionInstance,
            @NotNull ServiceRegistry<OrionInstance> orionServiceRegistry) {
        return new HeartbeatErrorHandler(thisOrionInstance, orionServiceRegistry);
    }

    private @NotNull Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, orionKafkaConfigurationProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, orionKafkaConfigurationProperties.getTrustedPackages());
        return props;
    }
}