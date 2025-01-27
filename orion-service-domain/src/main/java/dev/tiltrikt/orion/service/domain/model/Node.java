package dev.tiltrikt.orion.service.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Node {

    int id;

    int leaseDuration;

    @NonFinal
    boolean isLeader;
}
