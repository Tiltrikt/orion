package dev.tiltrikt.orion.service.kafka.consumer.manager;

import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerLeadershipManager;
import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import dev.tiltrikt.orion.service.common.event.BecomeFollowerEvent;
import dev.tiltrikt.orion.service.common.event.BecomeLeaderEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerLeadershipManager implements ConsumerLeadershipManager {

    @Qualifier("kafkaInstanceEventConsumerManager")
    @NotNull ConsumerManager instanceEventConsumerManager;

    @Qualifier("kafkaReplicationConsumerManager")
    @NotNull ConsumerManager replicationConsumerManager;

    @Override
    @EventListener
    public void becomeLeader(@NotNull BecomeLeaderEvent event) {
        replicationConsumerManager.stopListening();
        instanceEventConsumerManager.startListening();
    }

    @Override
    @EventListener
    public void becomeFollower(@NotNull BecomeFollowerEvent event) {
        instanceEventConsumerManager.stopListening();
        replicationConsumerManager.startListening();
    }
}
