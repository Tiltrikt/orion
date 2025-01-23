package dev.tiltrikt.orion.client.kafka.autoconfiguration;

import dev.tiltrikt.orion.client.domain.client.OrionDiscoveryClient;
import dev.tiltrikt.orion.client.domain.client.OrionReactiveDiscoveryClient;
import dev.tiltrikt.orion.client.domain.repository.RegistryRepository;
import dev.tiltrikt.orion.client.domain.repository.RegistryRepositoryImpl;
import dev.tiltrikt.orion.client.kafka.consumer.FetchRegistryConsumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnDiscoveryEnabled
@ConditionalOnProperty(value = "orion.client.fetch-registry", matchIfMissing = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FetchRegistryAutoconfiguration {

    @Bean
    @NotNull FetchRegistryConsumer fetchRegistryConsumer(@NotNull RegistryRepository registryRepository) {
        return new FetchRegistryConsumer(registryRepository);
    }

    @Bean
    @NotNull RegistryRepository registryRepository() {
        return new RegistryRepositoryImpl();
    }

    @Bean
    @NotNull DiscoveryClient discoveryClient(@NotNull RegistryRepository registryRepository) {
        return new OrionDiscoveryClient(registryRepository);
    }

    @ConditionalOnReactiveDiscoveryEnabled
    public static class ReactiveDiscoveryClientAutoconfiguration {

        @Bean
        @NotNull ReactiveDiscoveryClient reactiveDiscoveryClient(@NotNull RegistryRepository registryRepository) {
            return new OrionReactiveDiscoveryClient(registryRepository);
        }
    }
}
