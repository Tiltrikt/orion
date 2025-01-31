package dev.tiltrikt.orion.service.domain.raft;

public enum NodeState {
    LEADER,
    CANDIDATE,
    FOLLOWER
}