package dev.tiltrikt.orion.service.kafka.configuration;

import dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.service.common.consumer.manager.ConsumerManager;
import dev.tiltrikt.orion.service.common.publisher.HeartbeatErrorPublisher;
import dev.tiltrikt.orion.service.domain.exception.InstanceNotFoundException;
import dev.tiltrikt.orion.service.domain.handler.NodeHeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.follover.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.model.OrionServiceNode;
import dev.tiltrikt.orion.service.domain.model.factory.InstanceModelFactory;
import dev.tiltrikt.orion.service.kafka.consumer.manager.KafkaInstanceEventConsumerManager;
import dev.tiltrikt.orion.service.kafka.consumer.manager.KafkaNodeEventConsumerManager;
import dev.tiltrikt.orion.service.kafka.consumer.manager.KafkaReplicationConsumerManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerManagerConfiguration {

    @Bean
    @NotNull ConsumerManager kafkaInstanceEventConsumerManager(
            @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            @NotNull KafkaListenerContainerFactory kafkaListenerContainerFactory,
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull RegistrationHandler registrationHandler,
            @NotNull DeregistrationHandler deregistrationHandler,
            @NotNull HeartbeatHandler heartbeatHandler,
            @NotNull InstanceModelFactory instanceModelFactory
    ) {
        return new KafkaInstanceEventConsumerManager(
                kafkaListenerEndpointRegistry,
                kafkaListenerContainerFactory,
                thisOrionServiceNode,
                registrationHandler,
                deregistrationHandler,
                heartbeatHandler,
                instanceModelFactory
        );
    }

    @Bean
    @NotNull ConsumerManager kafkaNodeEventConsumerManager(
            @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            @NotNull KafkaListenerContainerFactory kafkaListenerContainerFactory,
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull NodeHeartbeatHandler nodeHeartbeatHandler
    ) {
        return new KafkaNodeEventConsumerManager(
                kafkaListenerEndpointRegistry,
                kafkaListenerContainerFactory,
                thisOrionServiceNode,
                nodeHeartbeatHandler
        );
    }

    @Bean
    @NotNull ConsumerManager kafkaReplicationConsumerManager(
            @NotNull KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            @NotNull KafkaListenerContainerFactory kafkaListenerContainerFactory,
            @NotNull OrionServiceNode thisOrionServiceNode,
            @NotNull ReplicationHandler replicationHandler
    ) {
        return new KafkaReplicationConsumerManager(
                kafkaListenerEndpointRegistry,
                kafkaListenerContainerFactory,
                thisOrionServiceNode,
                replicationHandler
        );
    }

    @Bean
    public DefaultErrorHandler errorHandler(@NotNull HeartbeatErrorPublisher errorPublisher) {
        BackOff fixedBackOff = new FixedBackOff(1, 0);
        return new DefaultErrorHandler((consumerRecord, exception) -> {
            if (exception.getCause() instanceof InstanceNotFoundException) {
                errorPublisher.publishError(((InstanceHeartbeatEvent) consumerRecord.value()).getInstanceId());
            }
        }, fixedBackOff);
    }
}
