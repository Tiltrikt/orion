package dev.tiltrikt.orion.service.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LeaseModelFactory {

    public @NotNull LeaseModel create(@NotNull String instanceId, int leaseDuration) {
        return new LeaseModel(
                instanceId,
                leaseDuration,
                Instant.now().plusSeconds(leaseDuration)
        );
    }
}
