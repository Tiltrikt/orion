package dev.tiltrikt.orion.service.kafka.manager;

import dev.tiltrikt.orion.common.configuration.KafkaTopicConfiguration;
import dev.tiltrikt.orion.common.event.NodeHeartbeatEvent;
import dev.tiltrikt.orion.common.event.ReplicationRegistryUpdateEvent;
import dev.tiltrikt.orion.service.common.manager.ConsumerManager;
import dev.tiltrikt.orion.service.domain.follower.handler.node.update.NodeUpdateHandler;
import dev.tiltrikt.orion.service.domain.follower.handler.replication.ReplicationHandler;
import dev.tiltrikt.orion.service.domain.follower.model.Node;
import dev.tiltrikt.orion.service.domain.leader.handler.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.leader.handler.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.leader.handler.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.kafka.consumer.KafkaInstanceConsumer;
import dev.tiltrikt.orion.service.kafka.consumer.KafkaNodeUpdateConsumer;
import dev.tiltrikt.orion.service.kafka.consumer.KafkaReplicationConsumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerManager implements ConsumerManager {

    @NotNull Map<String, Object> consumerConfigs;

    @NotNull RegistrationHandler registrationHandler;

    @NotNull DeregistrationHandler deregistrationHandler;

    @NotNull HeartbeatHandler heartbeatHandler;

    @NotNull ReplicationHandler replicationHandler;

    @NotNull NodeUpdateHandler nodeUpdateHandler;

    @NotNull Node thisNode;

    @NotNull KafkaListenerEndpointRegistry registry;

    @Override
    public void becomeLeader() {
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        System.out.println("becomeLeader");
        MessageListenerContainer oldContainer = registry.getListenerContainer("kafka-replication-consumer");
        if (oldContainer != null && oldContainer.isRunning()) {
            oldContainer.stop();
        }
        ContainerProperties containerProps = new ContainerProperties(KafkaTopicConfiguration.INSTANCE_REGISTRY_TOPIC);
        containerProps.setGroupId("orion-service");
        containerProps.setClientId("kafka-instance-consumer");
        containerProps.setMessageListener(new KafkaInstanceConsumer(registrationHandler, deregistrationHandler, heartbeatHandler));
        DefaultKafkaConsumerFactory<String, Object> cf = new DefaultKafkaConsumerFactory<>(
                consumerConfigs,
                new StringDeserializer(),
                new JsonDeserializer<>(Object.class)
        );
        KafkaMessageListenerContainer<String, Object> container = new KafkaMessageListenerContainer<>(cf, containerProps);
        container.start();
    }

    @Override
    public void becomeFollower() {
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        System.out.println("BecomeFollower");
        MessageListenerContainer oldContainer = registry.getListenerContainer("kafka-instance-consumer");
        if (oldContainer != null && oldContainer.isRunning()) {
            oldContainer.stop();
        }

        ContainerProperties containerProps = new ContainerProperties(KafkaTopicConfiguration.NODE_REGISTRY_TOPIC);
        containerProps.setGroupId(String.valueOf(thisNode.getId()));
        containerProps.setClientId("kafka-node-update-consumer");
        containerProps.setMessageListener(new KafkaNodeUpdateConsumer(nodeUpdateHandler));
        DefaultKafkaConsumerFactory<String, NodeHeartbeatEvent> cfNode = new DefaultKafkaConsumerFactory<>(
                consumerConfigs,
                new StringDeserializer(),
                new JsonDeserializer<>(NodeHeartbeatEvent.class)
        );
        KafkaMessageListenerContainer<String, NodeHeartbeatEvent> containerNode = new KafkaMessageListenerContainer<>(cfNode, containerProps);
        containerNode.start();

        containerProps = new ContainerProperties(KafkaTopicConfiguration.REPLICATION_REGISTRY_TOPIC);
        containerProps.setGroupId(String.valueOf(thisNode.getId()));
        containerProps.setClientId("kafka-replication-consumer");
        containerProps.setMessageListener(new KafkaReplicationConsumer(replicationHandler));
        DefaultKafkaConsumerFactory<String, ReplicationRegistryUpdateEvent> cfReplication = new DefaultKafkaConsumerFactory<>(
                consumerConfigs,
                new StringDeserializer(),
                new JsonDeserializer<>(ReplicationRegistryUpdateEvent.class)
        );
        KafkaMessageListenerContainer<String, ReplicationRegistryUpdateEvent> containerReplication = new KafkaMessageListenerContainer<>(cfReplication, containerProps);
        containerReplication.start();
    }
}