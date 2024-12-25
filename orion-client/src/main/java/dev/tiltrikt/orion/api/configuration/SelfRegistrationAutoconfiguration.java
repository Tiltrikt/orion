package dev.tiltrikt.orion.api.configuration;

import dev.tiltrikt.orion.api.event.InstanceDeregistrationEvent;
import dev.tiltrikt.orion.api.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.api.event.InstanceRegistrationEvent;
import dev.tiltrikt.orion.api.job.HeartbeatJob;
import dev.tiltrikt.orion.api.model.OrionInstance;
import dev.tiltrikt.orion.api.registration.OrionAutoRegistration;
import dev.tiltrikt.orion.api.registration.service.OrionServiceRegistry;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;

@Configuration
@EnableScheduling
@ConditionalOnDiscoveryEnabled
@ConditionalOnProperty(value = "orion.client.self-registration", matchIfMissing = true)
public class SelfRegistrationAutoconfiguration {

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
            @NotNull OrionConfigurationProperties properties,
            @NotNull KafkaTemplate<String, InstanceRegistrationEvent> registrationKafkaTemplate,
            @NotNull KafkaTemplate<String, InstanceDeregistrationEvent> deregistrationKafkaTemplate) {
        return new OrionServiceRegistry(registrationKafkaTemplate, deregistrationKafkaTemplate, properties);
    }

    @Bean
    @NotNull OrionAutoRegistration orionAutoRegistration(
            @NotNull ServiceRegistry<OrionInstance> serviceRegistry,
            @NotNull AutoServiceRegistrationProperties properties,
            @NotNull OrionInstance thisOrionInstance) {
        return new OrionAutoRegistration(serviceRegistry, properties, thisOrionInstance);
    }

    @Bean
    @NotNull HeartbeatJob heartbeatJob(
            @NotNull OrionInstance thisOrionInstance,
            @NotNull KafkaTemplate<String, InstanceHeartbeatEvent> kafkaTemplate
    ) {
        return new HeartbeatJob(kafkaTemplate, thisOrionInstance);
    }


}
