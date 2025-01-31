package dev.tiltrikt.orion.service.domain.raft;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Leader implements State {

    @Override
    public void execute() {

    }
}
