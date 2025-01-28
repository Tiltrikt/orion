package dev.tiltrikt.orion.common.configuration;

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

    public static final String REGISTRY_TOPIC = "fetch-registry";
    public static final String REPLICATION_TOPIC = "data-replication";
    public static final String NODE_EVENT_TOPIC = "node-event";
    public static final String INSTANCE_EVENT_TOPIC = "instance-event";
    public static final String HEARTBEAT_ERROR_TOPIC = "heartbeat-error";

    @Bean
    @NotNull NewTopic heartbeatErrorTopic() {
        return TopicBuilder
                .name(HEARTBEAT_ERROR_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    @NotNull NewTopic instanceEventTopic() {
        return TopicBuilder
                .name(INSTANCE_EVENT_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    @NotNull NewTopic replicationTopic() {
        return TopicBuilder
                .name(REPLICATION_TOPIC)
                .partitions(1)
                .replicas(1)
                .compact()
                .config(TopicConfig.MIN_COMPACTION_LAG_MS_CONFIG, "1")
                .config(TopicConfig.MAX_COMPACTION_LAG_MS_CONFIG, "100")
                .config(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG, "0.001")
                .config(TopicConfig.SEGMENT_MS_CONFIG, "10000")
                .config(TopicConfig.DELETE_RETENTION_MS_CONFIG, "30000")
                .build();
    }

    @Bean
    @NotNull NewTopic nodeEventTopic() {
        return TopicBuilder
                .name(NODE_EVENT_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    @NotNull NewTopic registryTopic() {
        return TopicBuilder
                .name(REGISTRY_TOPIC)
                .partitions(1)
                .replicas(1)
                .compact()
                .config(TopicConfig.MIN_COMPACTION_LAG_MS_CONFIG, "1")
                .config(TopicConfig.MAX_COMPACTION_LAG_MS_CONFIG, "100")
                .config(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG, "0.001")
                .config(TopicConfig.SEGMENT_MS_CONFIG, "10000")
                .config(TopicConfig.DELETE_RETENTION_MS_CONFIG, "30000")
                .build();
    }
}