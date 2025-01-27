package dev.tiltrikt.orion.service.kafka.consumer;

import dev.tiltrikt.orion.common.event.ReplicationEvent;
import dev.tiltrikt.orion.service.domain.handler.follover.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.listener.MessageListener;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaReplicationConsumer implements MessageListener<String, ReplicationEvent> {

    @NotNull ReplicationHandler replicationHandler;

    public void receive(@NotNull ReplicationEvent event) {
        InstanceModel instanceModel = new InstanceModel(
                event.getInstanceId(),
                event.getServiceId(),
                event.getHost(),
                event.getPort(),
                event.getMetadata(),
                event.getLeaseDuration(),
                event.getLeaseExpirationTime()
        );
        replicationHandler.replicate(instanceModel);
    }

    @Override
    public void onMessage(@NotNull ConsumerRecord<String, ReplicationEvent> record) {
        receive(record.value());
    }
}
