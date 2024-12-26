package dev.tiltrikt.orion.api.autoconfiguration;

import dev.tiltrikt.orion.api.configuration.OrionClientConfigurationProperties;
import dev.tiltrikt.orion.api.configuration.OrionKafkaConfigurationProperties;
import dev.tiltrikt.orion.api.job.HeartbeatJob;
import dev.tiltrikt.orion.api.model.OrionInstance;
import dev.tiltrikt.orion.api.registration.OrionAutoRegistration;
import dev.tiltrikt.orion.api.registration.service.OrionServiceRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnDiscoveryEnabled
@ConditionalOnProperty(value = "orion.client.self-registration", matchIfMissing = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SelfRegistrationAutoconfiguration {

    @NotNull OrionKafkaConfigurationProperties orionKafkaConfigurationProperties;

    @Bean
    @NotNull KafkaTemplateFactory kafkaTemplateFactory() {
        return new KafkaTemplateFactory();
    }

    @Bean
    @NotNull OrionInstance thisOrionInstance(
            @NotNull InetUtils inetUtils,
            @Value("${spring.application.name}") @NotNull String serviceId,
            @Value("${server.port}") int port) {
        return new OrionInstance(
                serviceId,
                inetUtils.findFirstNonLoopbackAddress().getHostAddress(),
                port,
                new HashMap<>()
        );
    }

    @Bean
    @NotNull ServiceRegistry<OrionInstance> serviceRegistry(
            @NotNull OrionClientConfigurationProperties properties,
            @NotNull KafkaTemplateFactory kafkaTemplateFactory) {
        return new OrionServiceRegistry(
                kafkaTemplateFactory.create(kafkaTemplateConfigurationProperties()),
                kafkaTemplateFactory.create(kafkaTemplateConfigurationProperties()),
                properties
        );
    }

    @Bean
    @NotNull OrionAutoRegistration orionAutoRegistration(
            @NotNull ServiceRegistry<OrionInstance> serviceRegistry,
            @NotNull AutoServiceRegistrationProperties properties,
            @NotNull OrionInstance thisOrionInstance) {
        return new OrionAutoRegistration(serviceRegistry, properties, thisOrionInstance);
    }

    private @NotNull Map<String, Object> kafkaTemplateConfigurationProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, orionKafkaConfigurationProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    @NotNull HeartbeatJob heartbeatJob(
            @NotNull OrionInstance thisOrionInstance,
            @NotNull KafkaTemplateFactory kafkaTemplateFactory
    ) {
        return new HeartbeatJob(kafkaTemplateFactory.create(kafkaTemplateConfigurationProperties()), thisOrionInstance);
    }
}