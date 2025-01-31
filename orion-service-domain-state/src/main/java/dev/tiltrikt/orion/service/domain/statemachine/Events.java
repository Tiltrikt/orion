package dev.tiltrikt.orion.service.domain.statemachine;

public enum Events {
    ELECTION_TIMEOUT,
    WIN_ELECTION,
    RECEIVE_APPEND_ENTRIES,
    STEP_DOWN
}