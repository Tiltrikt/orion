package dev.tiltrikt.orion.service.domain.service;

public interface VoteCounterService {

    void addVote(int voterId);

    void resetCounter();

    int getCounter();
}
