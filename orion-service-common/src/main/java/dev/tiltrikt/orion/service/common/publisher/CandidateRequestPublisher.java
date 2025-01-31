package dev.tiltrikt.orion.service.common.publisher;

import dev.tiltrikt.orion.service.common.event.CandidateRequestEvent;
import org.jetbrains.annotations.NotNull;

public interface CandidateRequestPublisher {

    void publish(@NotNull CandidateRequestEvent event);
}
