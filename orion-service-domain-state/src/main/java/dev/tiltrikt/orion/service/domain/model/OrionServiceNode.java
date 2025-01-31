package dev.tiltrikt.orion.service.domain.model;

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

    @NonFinal
    boolean isLeader;

    @NonFinal
    int term;

    public void increaseTerm() {
        term++;
    }
}
