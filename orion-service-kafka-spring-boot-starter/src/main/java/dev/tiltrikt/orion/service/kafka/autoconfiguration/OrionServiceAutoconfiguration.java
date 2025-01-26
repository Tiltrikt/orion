package dev.tiltrikt.orion.service.kafka.autoconfiguration;

import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.common.manager.ConsumerManager;
import dev.tiltrikt.orion.service.common.publisher.RegistryUpdatePublisher;
import dev.tiltrikt.orion.service.domain.follower.handler.node.update.NodeUpdateHandler;
import dev.tiltrikt.orion.service.domain.follower.handler.node.update.NodeUpdateHandlerImpl;
import dev.tiltrikt.orion.service.domain.follower.handler.replication.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.follower.handler.replication.ReplicationHandlerImpl;
import dev.tiltrikt.orion.service.domain.follower.model.Node;
import dev.tiltrikt.orion.service.domain.follower.service.NodeService;
import dev.tiltrikt.orion.service.domain.leader.handler.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.leader.handler.deregistration.DeregistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.leader.handler.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.leader.handler.heartbeat.HeartbeatHandlerImpl;
import dev.tiltrikt.orion.service.domain.leader.handler.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.leader.handler.registration.RegistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.leader.job.LeaseExpirationCheckJob;
import dev.tiltrikt.orion.service.domain.repository.InstanceRepository;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import dev.tiltrikt.orion.service.domain.service.InstanceServiceImpl;
import dev.tiltrikt.orion.service.kafka.manager.KafkaConsumerManager;
import dev.tiltrikt.orion.service.kafka.publisher.KafkaRegistryUpdatePublisher;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrionServiceAutoconfiguration {

    @Bean
    @NotNull ConsumerManager consumerManager(
            @Qualifier("consumerConfigs") @NotNull Map<String, Object> consumerConfigs,
            @NotNull RegistrationHandler registrationHandler,
            @NotNull DeregistrationHandler deregistrationHandler,
            @NotNull HeartbeatHandler heartbeatHandler,
            @NotNull ReplicationHandler replicationHandler,
            @NotNull NodeUpdateHandler nodeUpdateHandler,
            @NotNull Node thisNode,
            @NotNull KafkaListenerEndpointRegistry registry) {
        return new KafkaConsumerManager(
                consumerConfigs,
                registrationHandler,
                deregistrationHandler,
                heartbeatHandler,
                replicationHandler,
                nodeUpdateHandler,
                thisNode,
                registry
        );
    }

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
    @NotNull Node nodeModel() {
        int randomNumber = 1000 + (int) (Math.random() * 9000);
        return new Node(randomNumber, 30, false);
    }

    @Bean
    @NotNull RegistryUpdatePublisher eventPublisher(@NotNull KafkaTemplate<String, RegistryUpdateEvent> kafkaTemplate) {
        return new KafkaRegistryUpdatePublisher(kafkaTemplate);
    }

    @Bean
    @NotNull InstanceService instanceService(@NotNull InstanceRepository instanceRepository) {
        return new InstanceServiceImpl(instanceRepository);
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
    @NotNull NodeUpdateHandler nodeUpdateHandler(@NotNull NodeService nodeService) {
        return new NodeUpdateHandlerImpl(nodeService);
    }

}
