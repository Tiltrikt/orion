package dev.tiltrikt.orion.service.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

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
