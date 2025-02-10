package dev.tiltrikt.orion.service.domain.handler.leader.deregistration;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DeregistrationHandler {

    void deregister(@NotNull String instanceId);

    void deregisterBatch(@NotNull List<String> instanceIdList);
}
