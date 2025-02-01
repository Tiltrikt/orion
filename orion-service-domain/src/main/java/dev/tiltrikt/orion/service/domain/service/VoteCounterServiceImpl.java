package dev.tiltrikt.orion.service.domain.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteCounterServiceImpl implements VoteCounterService {

    @NotNull Set<Integer> votedIds = new HashSet<>();

    @Override
    public void addVote(int voterId) {
        votedIds.add(voterId);
    }

    @Override
    public void resetCounter() {
        votedIds.clear();
    }

    @Override
    public int getCounter() {
        return votedIds.size();
    }
}
