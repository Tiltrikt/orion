package dev.tiltrikt.orion.service.kafka.consumer.manager;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.model.Node;
import dev.tiltrikt.orion.service.kafka.consumer.KafkaInstanceEventConsumer;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaInstanceEventConsumerManager extends KafkaAbstractConsumerManager {

    private final static String KAFKA_INSTANCE_EVENT_CONSUMER = "kafka-instance-event-consumer";

    @NotNull RegistrationHandler registrationHandler;

    @NotNull DeregistrationHandler deregistrationHandler;

    @NotNull HeartbeatHandler heartbeatHandler;

    public KafkaInstanceEventConsumerManager(
            @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            @NotNull KafkaListenerContainerFactory kafkaListenerContainerFactory,
            @NotNull Node thisNode,
            @NotNull RegistrationHandler registrationHandler,
            @NotNull DeregistrationHandler deregistrationHandler,
            @NotNull HeartbeatHandler heartbeatHandler
    ) {
        super(kafkaListenerEndpointRegistry, kafkaListenerContainerFactory, thisNode);
        this.registrationHandler = registrationHandler;
        this.deregistrationHandler = deregistrationHandler;
        this.heartbeatHandler = heartbeatHandler;
    }

    @Override
    @SneakyThrows
    public void startListening() {
        MethodKafkaListenerEndpoint<String, Object> kafkaListenerEndpoint = createDefaultMethodKafkaListenerEndpoint(
                KafkaTopicConfiguration.INSTANCE_EVENT_TOPIC,
                KAFKA_INSTANCE_EVENT_CONSUMER,
                "orion-service"
        );
        kafkaListenerEndpoint.setBean(new KafkaInstanceEventConsumer(registrationHandler, deregistrationHandler, heartbeatHandler));
        kafkaListenerEndpoint.setMethod(KafkaInstanceEventConsumer.class.getMethod("onMessage", ConsumerRecord.class));
        kafkaListenerEndpointRegistry.registerListenerContainer(kafkaListenerEndpoint, kafkaListenerContainerFactory, true);
    }

    @Override
    public void stopListening() {
        destroyContainer(KAFKA_INSTANCE_EVENT_CONSUMER);
    }
}
