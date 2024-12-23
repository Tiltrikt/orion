package dev.tiltrikt.orion.api.runner;

import dev.tiltrikt.orion.api.repository.RegistryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationRunner {

    @NotNull RegistryRepository registryRepository;

    @Scheduled(fixedRate = 5000)
    public void run() {
        for (ServiceInstance serviceInstance : registryRepository.getAll()) {
            System.out.println(serviceInstance);
        }

    }
}
