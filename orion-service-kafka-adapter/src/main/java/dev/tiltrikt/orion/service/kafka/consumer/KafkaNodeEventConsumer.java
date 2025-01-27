package dev.tiltrikt.orion.service.kafka.consumer;

import dev.tiltrikt.orion.common.event.NodeHeartbeatEvent;
import dev.tiltrikt.orion.service.domain.handler.NodeHeartbeatHandler;
import dev.tiltrikt.orion.service.domain.model.NodeModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.listener.MessageListener;

import java.time.Instant;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaNodeEventConsumer implements MessageListener<String, NodeHeartbeatEvent> {

    @NotNull NodeHeartbeatHandler nodeHeartbeatHandler;

    public void receive(@NotNull NodeHeartbeatEvent event) {
        NodeModel nodeModel = new NodeModel(
                event.getId(),
                event.getLeaseDuration(),
                Instant.now().plusSeconds(event.getLeaseDuration()),
                Boolean.parseBoolean(event.getIsLeader())
        );
        nodeHeartbeatHandler.update(nodeModel);
    }

    @Override
    public void onMessage(@NotNull ConsumerRecord<String, NodeHeartbeatEvent> record) {
        receive(record.value());
    }
}