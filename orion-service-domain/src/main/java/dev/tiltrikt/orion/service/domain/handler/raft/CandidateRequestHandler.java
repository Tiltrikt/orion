package dev.tiltrikt.orion.service.domain.handler.raft;

import dev.tiltrikt.orion.service.common.event.CandidateRequestEvent;
import org.jetbrains.annotations.NotNull;

public interface CandidateRequestHandler {

    void handle(@NotNull CandidateRequestEvent event);
}
