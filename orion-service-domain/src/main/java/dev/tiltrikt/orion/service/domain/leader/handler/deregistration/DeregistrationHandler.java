package dev.tiltrikt.orion.service.domain.leader.handler.deregistration;

import org.jetbrains.annotations.NotNull;

public interface DeregistrationHandler {

    void deregister(@NotNull String instanceId);
}
