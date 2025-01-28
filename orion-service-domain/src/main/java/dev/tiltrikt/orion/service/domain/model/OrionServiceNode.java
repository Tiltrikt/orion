package dev.tiltrikt.orion.service.domain.model;

import dev.tiltrikt.orion.service.common.event.BecomeFollowerEvent;
import dev.tiltrikt.orion.service.common.event.BecomeLeaderEvent;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrionServiceNode {

    int id;

    int leaseDuration;

    @NonFinal
    boolean isLeader;

    @EventListener
    public void handleBecomeLeaderEvent(@NotNull BecomeLeaderEvent event) {
        isLeader = true;
    }

    @EventListener
    public void handleBecomeFollowerEvent(@NotNull BecomeFollowerEvent event) {
        isLeader = false;
    }
}
