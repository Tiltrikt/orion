package dev.tiltrikt.orion.api.registry;

import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface RegistryRepository {

    @NotNull List<ServiceInstance> getAll();

    @NotNull List<ServiceInstance> getAllByServiceId(@NotNull String serviceId);

    @NotNull ServiceInstance save(@NotNull ServiceInstance serviceInstance);

    void deleteById(@NotNull String instanceId);
}
