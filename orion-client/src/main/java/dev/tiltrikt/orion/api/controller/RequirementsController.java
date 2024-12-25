package dev.tiltrikt.orion.api.controller;

import dev.tiltrikt.orion.api.model.OrionInstance;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@ConditionalOnBean({ServiceRegistry.class, OrionInstance.class})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequirementsController {

    @NotNull ServiceRegistry<OrionInstance> serviceRegistry;

    @NotNull OrionInstance thisOrionInstance;

    @PutMapping(value = "/require/registration")
    public void registration() {
        serviceRegistry.register(thisOrionInstance);
    }
}
