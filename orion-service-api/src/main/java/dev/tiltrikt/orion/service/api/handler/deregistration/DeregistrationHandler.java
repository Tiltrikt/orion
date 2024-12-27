package dev.tiltrikt.orion.service.api.handler.deregistration;

import org.jetbrains.annotations.NotNull;

public interface DeregistrationHandler {

    void deregister(@NotNull String instanceId);
}
