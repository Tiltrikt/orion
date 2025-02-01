package dev.tiltrikt.orion.service.domain.handler.raft;

import dev.tiltrikt.orion.service.common.event.VoteEvent;
import org.jetbrains.annotations.NotNull;

public interface VoteRequestHandler {

    void handle(@NotNull VoteEvent event);
}
