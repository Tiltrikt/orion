package dev.tiltrikt.orion.service.kafka.autoconfiguration;

import dev.tiltrikt.orion.common.event.InstanceHeartbeatEvent;
import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.common.publisher.ReplicationRegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.exception.InstanceNotFoundException;
import dev.tiltrikt.orion.service.domain.handler.NodeHeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.NodeHeartbeatHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.follover.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.handler.follover.ReplicationHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.deregistration.DeregistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.heartbeat.HeartbeatHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.leader.registration.RegistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.job.LeaseExpirationCheckJob;
import dev.tiltrikt.orion.service.domain.model.Node;
import dev.tiltrikt.orion.service.domain.repository.InstanceRepository;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import dev.tiltrikt.orion.service.domain.service.InstanceServiceImpl;
import dev.tiltrikt.orion.service.domain.service.NodeService;
import dev.tiltrikt.orion.service.kafka.publisher.KafkaHeartbeatErrorPublisher;
import dev.tiltrikt.orion.service.kafka.publisher.KafkaRegistryUpdatePublisher;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrionServiceAutoconfiguration {

    @Bean
    @NotNull Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "dev.tiltrikt.orion.common.event");
        return props;
    }

    @Bean
    public DefaultErrorHandler errorHandler(@NotNull KafkaHeartbeatErrorPublisher errorPublisher) {
        BackOff fixedBackOff = new FixedBackOff(1, 0);
        return new DefaultErrorHandler((consumerRecord, exception) -> {
            if (exception.getCause() instanceof InstanceNotFoundException) {
                errorPublisher.publishError(((InstanceHeartbeatEvent) consumerRecord.value()).getInstanceId());
            }
        }, fixedBackOff);
    }

    @Bean
    @NotNull Node nodeModel() {
        int randomNumber = 1000 + (int) (Math.random() * 9000);
        return new Node(randomNumber, 30, false);
    }

    @Bean
    @NotNull RegistryUpdatePublisher eventPublisher(@NotNull KafkaTemplate<String, RegistryUpdateEvent> kafkaTemplate) {
        return new KafkaRegistryUpdatePublisher(kafkaTemplate);
    }

    @Bean
    @NotNull InstanceService instanceService(
            @NotNull InstanceRepository instanceRepository,
            @NotNull ReplicationRegistryUpdatePublisher replicationRegistryUpdatePublisher) {
        return new InstanceServiceImpl(instanceRepository, replicationRegistryUpdatePublisher);
    }

    @Bean
    @NotNull RegistrationHandler registrationHandler(
            @NotNull RegistryUpdatePublisher registryUpdatePublisher,
            @NotNull InstanceService instanceService) {
        return new RegistrationHandlerImpl(instanceService, registryUpdatePublisher);
    }

    @Bean
    @NotNull DeregistrationHandler deregistrationHandler(
            @NotNull RegistryUpdatePublisher registryUpdatePublisher,
            @NotNull InstanceService instanceService) {
        return new DeregistrationHandlerImpl(instanceService, registryUpdatePublisher);
    }

    @Bean
    @NotNull HeartbeatHandler heartbeatHandler(
            @NotNull RegistryUpdatePublisher registryUpdatePublisher,
            @NotNull InstanceService instanceService) {
        return new HeartbeatHandlerImpl(instanceService, registryUpdatePublisher);
    }

    @Bean
    @NotNull LeaseExpirationCheckJob leaseExpirationCheckJob(
            @NotNull RegistryUpdatePublisher registryUpdatePublisher,
            @NotNull InstanceService instanceService) {
        return new LeaseExpirationCheckJob(registryUpdatePublisher, instanceService);
    }

    @Bean
    @NotNull ReplicationHandler replicationHandler(@NotNull InstanceService instanceService) {
        return new ReplicationHandlerImpl(instanceService);
    }

    @Bean
    @NotNull NodeHeartbeatHandler nodeUpdateHandler(@NotNull NodeService nodeService) {
        return new NodeHeartbeatHandlerImpl(nodeService);
    }

}
