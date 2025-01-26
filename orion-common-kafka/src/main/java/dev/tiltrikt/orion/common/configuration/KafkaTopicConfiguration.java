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

    public static final String FETCH_REGISTRY_TOPIC = "fetch-registry";
    public static final String REPLICATION_REGISTRY_TOPIC = "replication-registry";
    public static final String NODE_REGISTRY_TOPIC = "node-registry";
    public static final String INSTANCE_REGISTRY_TOPIC = "instance-registry";

    @Bean
    @NotNull NewTopic instanceRegistry() {
        return TopicBuilder
                .name(INSTANCE_REGISTRY_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    @NotNull NewTopic replicationRegistryTopic() {
        return TopicBuilder
                .name(REPLICATION_REGISTRY_TOPIC)
                .partitions(1)
                .replicas(1)
                .compact()
                .config(TopicConfig.MIN_COMPACTION_LAG_MS_CONFIG, "1")
                .config(TopicConfig.MAX_COMPACTION_LAG_MS_CONFIG, "100")
                .config(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG, "0.001")
                .config(TopicConfig.SEGMENT_MS_CONFIG, "10000")
                .build();
    }

    @Bean
    @NotNull NewTopic nodeRegistryTopic() {
        return TopicBuilder
                .name(NODE_REGISTRY_TOPIC)
                .partitions(1)
                .replicas(1)
                .compact()
                .config(TopicConfig.MIN_COMPACTION_LAG_MS_CONFIG, "1")
                .config(TopicConfig.MAX_COMPACTION_LAG_MS_CONFIG, "100")
                .config(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG, "0.001")
                .config(TopicConfig.SEGMENT_MS_CONFIG, "10000")
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