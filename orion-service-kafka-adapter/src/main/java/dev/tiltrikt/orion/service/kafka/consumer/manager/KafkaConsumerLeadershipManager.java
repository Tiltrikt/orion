package dev.tiltrikt.orion.service.kafka.consumer.manager;

import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerLeadershipManager;
import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerLeadershipManager implements ConsumerLeadershipManager {

    @Qualifier("kafkaInstanceEventConsumerManager")
    @NotNull ConsumerManager instanceEventConsumerManager;

    @Qualifier("kafkaReplicationConsumerManager")
    @NotNull ConsumerManager replicationConsumerManager;

    @Async
    @Override
    public void becomeLeader() {
        replicationConsumerManager.stopListening();
        instanceEventConsumerManager.startListening();
    }

    @Async
    @Override
    public void becomeFollower() {
        instanceEventConsumerManager.stopListening();
        replicationConsumerManager.startListening();
    }
}
