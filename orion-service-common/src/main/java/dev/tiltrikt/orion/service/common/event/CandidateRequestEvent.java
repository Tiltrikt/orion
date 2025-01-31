package dev.tiltrikt.orion.service.common.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@ToString
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidateRequestEvent {

    int candidateId;

    int term;
}
