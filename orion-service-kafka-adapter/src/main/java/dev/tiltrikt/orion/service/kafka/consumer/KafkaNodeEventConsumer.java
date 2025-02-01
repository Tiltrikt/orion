package dev.tiltrikt.orion.service.kafka.consumer;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.common.event.CandidateRequestEvent;
import dev.tiltrikt.orion.service.common.event.LeaderHeartbeatEvent;
import dev.tiltrikt.orion.service.common.event.VoteEvent;
import dev.tiltrikt.orion.service.domain.handler.raft.CandidateRequestHandler;
import dev.tiltrikt.orion.service.domain.handler.raft.LeaderHeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.raft.VoteRequestHandler;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.listener.MessageListener;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaNodeEventConsumer implements MessageListener<String, Object>, ConsumerSeekAware {

    @NotNull CandidateRequestHandler candidateRequestHandler;

    @NotNull LeaderHeartbeatHandler leaderHeartbeatHandler;

    @NotNull VoteRequestHandler voteRequestHandler;

    @NotNull OrionServiceNode thisOrionServiceNode;

    public void receiveHeartbeat(@NotNull LeaderHeartbeatEvent event) {
        log.info("Received heartbeat: {}", event);
        leaderHeartbeatHandler.handle(event);
    }

    public void receiveCandidateRequest(@NotNull CandidateRequestEvent event) {
        log.info("Received candidate request: {}", event);
        candidateRequestHandler.handle(event);
    }

    public void receiveVote(@NotNull VoteEvent event) {
        log.info("Received vote: {}", event);
        voteRequestHandler.handle(event);
    }

    @Override
    public void onMessage(@NotNull ConsumerRecord<String, Object> record) {
        if (record.key().equals(String.valueOf(thisOrionServiceNode.getId()))) {
            return;
        }
        if (record.value() instanceof LeaderHeartbeatEvent) {
            receiveHeartbeat((LeaderHeartbeatEvent) record.value());
        } else if (record.value() instanceof CandidateRequestEvent) {
            receiveCandidateRequest((CandidateRequestEvent) record.value());
        } else if (record.value() instanceof VoteEvent) {
            receiveVote((VoteEvent) record.value());
        }
    }

    @Override
    public void onPartitionsAssigned(
            @NotNull Map<TopicPartition, Long> assignments,
            @NotNull ConsumerSeekAware.ConsumerSeekCallback callback
    ) {
        for (TopicPartition topicPartition : assignments.keySet()) {
            if (topicPartition.topic().equals(KafkaTopicConfiguration.NODE_EVENT_TOPIC)) {
                callback.seekToEnd(topicPartition.topic(), topicPartition.partition());
            }
        }
    }
}