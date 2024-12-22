package dev.tiltrikt.orion.service.consumer.exception.handler;

import dev.tiltrikt.orion.api.event.InstanceHeartbeatEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeartbeatExceptionHandler implements KafkaListenerErrorHandler {

    @NotNull RestClient restClient;

    @Override
    public Object handleError(@NotNull Message<?> message, @NotNull ListenerExecutionFailedException exception) {
        log.warn(exception.getCause().getMessage());
        String instanceUri = ((InstanceHeartbeatEvent) message.getPayload()).getInstanceId();
        restClient.put()
                .uri("http://" + instanceUri + "/require/registration")
                .retrieve()
                .toBodilessEntity();
        return null;
    }
}
