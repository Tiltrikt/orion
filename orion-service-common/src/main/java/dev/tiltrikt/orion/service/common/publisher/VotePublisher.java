package dev.tiltrikt.orion.service.common.publisher;

import dev.tiltrikt.orion.service.common.event.VoteEvent;
import org.jetbrains.annotations.NotNull;

public interface VotePublisher {

    void publish(@NotNull VoteEvent event);
}
