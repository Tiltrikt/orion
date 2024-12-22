package dev.tiltrikt.orion.api.discovery.client;

import dev.tiltrikt.orion.api.repository.RegistryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrionDiscoveryClient implements DiscoveryClient {

    @NotNull RegistryRepository registryRepository;

    @Override
    public String description() {
        return "";
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        return registryRepository.getAllByServiceId(serviceId);
    }

    @Override
    public List<String> getServices() {
        return registryRepository.getAll().stream()
                .map(ServiceInstance::getServiceId)
                .collect(Collectors.toList());
    }
}
