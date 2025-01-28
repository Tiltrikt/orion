package dev.tiltrikt.orion.service.kafka.consumer.manager;

import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class KafkaAbstractConsumerManager implements ConsumerManager {

    @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @NotNull KafkaListenerContainerFactory kafkaListenerContainerFactory;

    @NotNull OrionServiceNode thisOrionServiceNode;

    protected void destroyContainer(@NotNull String id) {
        MessageListenerContainer messageListenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        if (messageListenerContainer != null) {
            messageListenerContainer.destroy();
        }
    }

    protected @NotNull <T> MethodKafkaListenerEndpoint<String, T> createDefaultMethodKafkaListenerEndpoint(
            @NotNull String topic,
            @NotNull String listenerId,
            @NotNull String groupId) {
        MethodKafkaListenerEndpoint<String, T> kafkaListenerEndpoint = new MethodKafkaListenerEndpoint<>();
        kafkaListenerEndpoint.setId(listenerId);
        kafkaListenerEndpoint.setGroupId(groupId);
        kafkaListenerEndpoint.setAutoStartup(true);
        kafkaListenerEndpoint.setTopics(topic);
        kafkaListenerEndpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
        return kafkaListenerEndpoint;
    }
}
