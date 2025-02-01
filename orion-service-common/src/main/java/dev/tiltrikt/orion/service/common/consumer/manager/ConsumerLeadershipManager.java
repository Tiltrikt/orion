package dev.tiltrikt.orion.service.common.consumer.manager;

public interface ConsumerLeadershipManager {

    void becomeLeader();

    void becomeFollower();
}
