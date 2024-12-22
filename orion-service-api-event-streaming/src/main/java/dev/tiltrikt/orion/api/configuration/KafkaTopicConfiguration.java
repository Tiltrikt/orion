package dev.tiltrikt.orion.api.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@Configuration
public class KafkaTopicConfiguration {

    public static final String INSTANCE_REGISTRATION_TOPIC = "instance-registration";
    public static final String INSTANCE_DEREGISTRATION_TOPIC = "instance-deregistration";
    public static final String INSTANCE_HEARTBEAT_TOPIC = "instance-heartbeat";
    public static final String FETCH_REGISTRY_TOPIC = "fetch-registry";

    @Bean
    @NotNull NewTopic instanceRegistrationTopic() {
        return TopicBuilder
                .name(INSTANCE_REGISTRATION_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }


    @Bean
    @NotNull NewTopic instanceDeregistrationTopic() {
        return TopicBuilder
                .name(INSTANCE_DEREGISTRATION_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    @NotNull NewTopic instanceHeartbeatTopic() {
        return TopicBuilder
                .name(INSTANCE_HEARTBEAT_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }


    @Bean
    @NotNull NewTopic fetchRegistryTopic() {
        return TopicBuilder
                .name(FETCH_REGISTRY_TOPIC)
                .partitions(1)
                .replicas(1)
                .compact()
                .config(TopicConfig.MIN_COMPACTION_LAG_MS_CONFIG, "1")
                .config(TopicConfig.MAX_COMPACTION_LAG_MS_CONFIG, "100")
                .config(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG, "0.001")
                .config(TopicConfig.SEGMENT_MS_CONFIG, "10000")
                .build();
    }
}