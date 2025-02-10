package dev.tiltrikt.orion.service.kafka.consumer;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.ReplicationEvent;
import dev.tiltrikt.orion.service.domain.handler.follower.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.model.InstanceModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaReplicationConsumer implements BatchMessageListener<String, ReplicationEvent>, ConsumerSeekAware {

    @NotNull ReplicationHandler replicationHandler;

    public void replicate(@NotNull List<ReplicationEvent> events) {
        List<InstanceModel> instanceModels = events.stream().map(event -> new InstanceModel(
                event.getInstanceId(),
                event.getServiceId(),
                event.getHost(),
                event.getPort(),
                event.getMetadata(),
                event.getLeaseDuration(),
                event.getLeaseExpirationTime(),
                event.getInstanceState()
        )).toList();

        for (InstanceModel instanceModel : instanceModels) {
            replicationHandler.replicate(instanceModel);
        }
    }

    public void delete(@NotNull List<String> instanceIds) {
        for (String instanceId : instanceIds) {
            replicationHandler.deleteById(instanceId);
        }
    }

    @Override
    public void onMessage(@NotNull List<ConsumerRecord<String, ReplicationEvent>> records) {
        for (ConsumerRecord<String, ReplicationEvent> record : records) {
            System.out.println("Replication data: " + record.value());
        }
        if (records.isEmpty()) return;

        Map<Boolean, List<ConsumerRecord<String, ReplicationEvent>>> groupedRecords = records.stream()
                .collect(Collectors.partitioningBy(record -> record.value() != null));

        List<ReplicationEvent> replicationEvents = groupedRecords.get(true).stream()
                .map(ConsumerRecord::value)
                .collect(Collectors.toList());
        if (!replicationEvents.isEmpty()) {
            replicate(replicationEvents);
        }

        List<String> deleteInstanceIds = groupedRecords.get(false).stream()
                .map(ConsumerRecord::key)
                .collect(Collectors.toList());
        if (!deleteInstanceIds.isEmpty()) {
            delete(deleteInstanceIds);
        }
    }

    @Override
    public void onPartitionsAssigned(
            @NotNull Map<TopicPartition, Long> assignments,
            @NotNull ConsumerSeekCallback callback
    ) {
        for (TopicPartition topicPartition : assignments.keySet()) {
            if (topicPartition.topic().equals(KafkaTopicConfiguration.REPLICATION_TOPIC)) {
                callback.seekToBeginning(topicPartition.topic(), topicPartition.partition());
            }
        }
    }
}
