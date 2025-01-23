package dev.tiltrikt.orion.client.domain.repository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistryRepositoryImpl implements RegistryRepository {

    @NotNull Map<String, ServiceInstance> instanceMap = new HashMap<>();

    @Override
    public @NotNull List<ServiceInstance> getAll() {
        return instanceMap.values().stream().toList();
    }

    @Override
    public @NotNull List<ServiceInstance> getAllByServiceId(@NotNull String serviceId) {
        return instanceMap.values().stream()
                .filter(i -> i.getServiceId().equals(serviceId))
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull ServiceInstance save(@NotNull ServiceInstance serviceInstance) {
        instanceMap.put(serviceInstance.getInstanceId(), serviceInstance);
        return serviceInstance;
    }

    @Override
    public void deleteById(@NotNull String instanceId) {
        instanceMap.remove(instanceId);
    }
}