package dev.tiltrikt.orion.service.kafka.consumer;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.ReplicationEvent;
import dev.tiltrikt.orion.service.domain.handler.follover.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.listener.MessageListener;

import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaReplicationConsumer implements MessageListener<String, ReplicationEvent>, ConsumerSeekAware {

    @NotNull ReplicationHandler replicationHandler;

    public void replicate(@NotNull ReplicationEvent event) {
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

    public void delete(@NotNull String instanceId) {
        replicationHandler.deleteById(instanceId);
    }

    @Override
    public void onMessage(@NotNull ConsumerRecord<String, ReplicationEvent> record) {
        System.out.println("Replication data: " + record.value());
        if (record.value() == null) {
            delete(record.key());
        } else {
            replicate(record.value());
        }
    }

    @Override
    public void onPartitionsAssigned(
            @NotNull Map<TopicPartition, Long> assignments,
            @NotNull ConsumerSeekAware.ConsumerSeekCallback callback
    ) {
        for (TopicPartition topicPartition : assignments.keySet()) {
            if (topicPartition.topic().equals(KafkaTopicConfiguration.REPLICATION_TOPIC)) {
                callback.seekToBeginning(topicPartition.topic(), topicPartition.partition());
            }
        }
    }
}
