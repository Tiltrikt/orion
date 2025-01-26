package dev.tiltrikt.orion.service.common.manager;

public interface ConsumerManager {

    void becomeLeader();

    void becomeFollower();
}
