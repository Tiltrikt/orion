package dev.tiltrikt.orion.api.discovery.client;

import dev.tiltrikt.orion.api.repository.RegistryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrionReactiveDiscoveryClient implements ReactiveDiscoveryClient {

    @NotNull RegistryRepository registryRepository;

    @Override
    public String description() {
        return "";
    }

    @Override
    public Flux<ServiceInstance> getInstances(String serviceId) {
        return Flux.defer(() -> Flux.fromIterable(registryRepository.getAllByServiceId(serviceId)));
    }

    @Override
    public Flux<String> getServices() {
        return Flux.defer(() -> Flux.fromIterable(registryRepository.getAll())
                .map(ServiceInstance::getServiceId));
    }
}