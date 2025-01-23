package dev.tiltrikt.orion.service.domain.handler.deregistration;

import org.jetbrains.annotations.NotNull;

public interface DeregistrationHandler {

    void deregister(@NotNull String instanceId);
}
