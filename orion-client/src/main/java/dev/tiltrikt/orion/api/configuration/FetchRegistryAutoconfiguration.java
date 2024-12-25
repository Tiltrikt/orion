package dev.tiltrikt.orion.api.configuration;

import dev.tiltrikt.orion.api.consumer.FetchRegistryConsumer;
import dev.tiltrikt.orion.api.discovery.client.OrionDiscoveryClient;
import dev.tiltrikt.orion.api.discovery.client.OrionReactiveDiscoveryClient;
import dev.tiltrikt.orion.api.repository.RegistryRepository;
import dev.tiltrikt.orion.api.repository.RegistryRepositoryImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnDiscoveryEnabled
@ConditionalOnProperty(value = "orion.client.fetch-registry", matchIfMissing = true)
public class FetchRegistryAutoconfiguration {

    @Bean
    @NotNull RegistryRepository registryRepository() {
        return new RegistryRepositoryImpl();
    }

    @Bean
    @NotNull FetchRegistryConsumer fetchRegistryConsumer(@NotNull RegistryRepository registryRepository) {
        return new FetchRegistryConsumer(registryRepository);
    }

    @ConditionalOnDiscoveryEnabled
    public static class DiscoveryClientAutoconfiguration {

        @Bean
        @NotNull DiscoveryClient discoveryClient(@NotNull RegistryRepository registryRepository) {
            return new OrionDiscoveryClient(registryRepository);
        }
    }

    @ConditionalOnReactiveDiscoveryEnabled
    public static class ReactiveDiscoveryClientAutoconfiguration {

        @Bean
        @NotNull ReactiveDiscoveryClient reactiveDiscoveryClient(@NotNull RegistryRepository registryRepository) {
            return new OrionReactiveDiscoveryClient(registryRepository);
        }
    }
}
