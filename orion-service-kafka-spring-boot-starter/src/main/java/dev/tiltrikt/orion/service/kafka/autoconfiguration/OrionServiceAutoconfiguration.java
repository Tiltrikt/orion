package dev.tiltrikt.orion.service.kafka.autoconfiguration;

import dev.tiltrikt.orion.common.event.RegistryUpdateEvent;
import dev.tiltrikt.orion.service.common.publisher.EventPublisher;
import dev.tiltrikt.orion.service.domain.handler.deregistration.DeregistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.deregistration.DeregistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.heartbeat.HeartbeatHandler;
import dev.tiltrikt.orion.service.domain.handler.heartbeat.HeartbeatHandlerImpl;
import dev.tiltrikt.orion.service.domain.handler.registration.RegistrationHandler;
import dev.tiltrikt.orion.service.domain.handler.registration.RegistrationHandlerImpl;
import dev.tiltrikt.orion.service.domain.job.LeaseExpirationCheckJob;
import dev.tiltrikt.orion.service.domain.repository.InstanceRepository;
import dev.tiltrikt.orion.service.domain.service.InstanceService;
import dev.tiltrikt.orion.service.domain.service.InstanceServiceImpl;
import dev.tiltrikt.orion.service.kafka.consumer.InstanceDeregistrationConsumer;
import dev.tiltrikt.orion.service.kafka.consumer.InstanceHeartbeatConsumer;
import dev.tiltrikt.orion.service.kafka.consumer.InstanceRegistrationConsumer;
import dev.tiltrikt.orion.service.kafka.publisher.KafkaEventPublisher;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class OrionServiceAutoconfiguration {

    @Bean
    @NotNull EventPublisher eventPublisher(@NotNull KafkaTemplate<String, RegistryUpdateEvent> kafkaTemplate) {
        return new KafkaEventPublisher(kafkaTemplate);
    }

    @Bean
    @NotNull InstanceService instanceService(@NotNull InstanceRepository instanceRepository) {
        return new InstanceServiceImpl(instanceRepository);
    }

    @Bean
    @NotNull RegistrationHandler registrationHandler(
            @NotNull EventPublisher eventPublisher,
            @NotNull InstanceService instanceService) {
        return new RegistrationHandlerImpl(instanceService, eventPublisher);
    }

    @Bean
    @NotNull DeregistrationHandler deregistrationHandler(
            @NotNull EventPublisher eventPublisher,
            @NotNull InstanceService instanceService) {
        return new DeregistrationHandlerImpl(instanceService, eventPublisher);
    }

    @Bean
    @NotNull HeartbeatHandler heartbeatHandler(
            @NotNull EventPublisher eventPublisher,
            @NotNull InstanceService instanceService) {
        return new HeartbeatHandlerImpl(instanceService, eventPublisher);
    }

    @Bean
    @NotNull LeaseExpirationCheckJob leaseExpirationCheckJob(
            @NotNull EventPublisher eventPublisher,
            @NotNull InstanceService instanceService) {
        return new LeaseExpirationCheckJob(eventPublisher, instanceService);
    }

    @Bean
    @NotNull InstanceDeregistrationConsumer instanceDeregistrationConsumer(@NotNull DeregistrationHandler deregistrationHandler) {
        return new InstanceDeregistrationConsumer(deregistrationHandler);
    }

    @Bean
    @NotNull InstanceRegistrationConsumer instanceRegistrationConsumer(@NotNull RegistrationHandler registrationHandler) {
        return new InstanceRegistrationConsumer(registrationHandler);
    }

    @Bean
    @NotNull InstanceHeartbeatConsumer instanceHeartbeatConsumer(@NotNull HeartbeatHandler heartbeatHandler) {
        return new InstanceHeartbeatConsumer(heartbeatHandler);
    }
}
