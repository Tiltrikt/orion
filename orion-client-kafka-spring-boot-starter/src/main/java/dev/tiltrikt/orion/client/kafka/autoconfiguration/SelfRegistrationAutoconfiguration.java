package dev.tiltrikt.orion.client.kafka.autoconfiguration;

import dev.tiltrikt.orion.client.common.publisher.EventPublisher;
import dev.tiltrikt.orion.client.domain.job.HeartbeatJob;
import dev.tiltrikt.orion.client.domain.model.OrionInstance;
import dev.tiltrikt.orion.client.domain.registration.OrionAutoRegistration;
import dev.tiltrikt.orion.client.domain.registration.service.OrionServiceRegistry;
import dev.tiltrikt.orion.client.kafka.configuration.OrionClientConfigurationProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnDiscoveryEnabled
@ConditionalOnProperty(value = "orion.client.self-registration", matchIfMissing = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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
    @NotNull OrionAutoRegistration orionAutoRegistration(
            @NotNull ServiceRegistry<OrionInstance> serviceRegistry,
            @NotNull AutoServiceRegistrationProperties properties,
            @NotNull OrionInstance thisOrionInstance) {
        return new OrionAutoRegistration(serviceRegistry, properties, thisOrionInstance);
    }

    @Bean
    @NotNull ServiceRegistry<OrionInstance> serviceRegistry(
            @NotNull OrionClientConfigurationProperties properties,
            @NotNull EventPublisher eventPublisher) {
        return new OrionServiceRegistry(
                eventPublisher,
                properties.getLeaseDurationSec()
        );
    }

    @Bean
    @NotNull HeartbeatJob heartbeatJob(
            @NotNull OrionInstance thisOrionInstance,
            @NotNull EventPublisher eventPublisher
    ) {
        return new HeartbeatJob(eventPublisher, thisOrionInstance);
    }
}