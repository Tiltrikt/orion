package dev.tiltrikt.orion.service.kafka.leadership;

import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import dev.tiltrikt.orion.service.common.leadership.LeadershipManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaLeadershipManager implements LeadershipManager {

    @Qualifier("kafkaInstanceEventConsumerManager")
    @NotNull ConsumerManager instanceEventConsumerManager;

    @Qualifier("kafkaReplicationConsumerManager")
    @NotNull ConsumerManager replicationConsumerManager;

    @Override
    public void becomeLeader() {
        replicationConsumerManager.stopListening();
        instanceEventConsumerManager.startListening();
    }

    @Override
    public void becomeFollower() {
        instanceEventConsumerManager.stopListening();
        replicationConsumerManager.startListening();
    }
}
