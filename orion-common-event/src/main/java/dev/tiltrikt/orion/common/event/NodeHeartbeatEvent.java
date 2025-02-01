package dev.tiltrikt.orion.common.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@ToString
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NodeHeartbeatEvent {

    int id;

    int leaseDuration;

    @NotNull String isLeader;
}