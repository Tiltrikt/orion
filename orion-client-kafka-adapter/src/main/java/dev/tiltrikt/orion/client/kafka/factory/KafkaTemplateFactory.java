package dev.tiltrikt.orion.client.kafka.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaTemplateFactory {

    public @NotNull <K, V> KafkaTemplate<K, V> create(@NotNull Map<String, Object> templateConfigurationProperties) {
        ProducerFactory<K, V> producerFactory = new DefaultKafkaProducerFactory<>(templateConfigurationProperties);
        return new KafkaTemplate<>(producerFactory);
    }
}
