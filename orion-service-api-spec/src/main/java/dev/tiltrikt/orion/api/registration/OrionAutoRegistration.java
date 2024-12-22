package dev.tiltrikt.orion.api.registration;

import dev.tiltrikt.orion.api.model.OrionInstance;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrionAutoRegistration extends AbstractAutoServiceRegistration<OrionInstance> {

    protected OrionAutoRegistration(
            @NotNull ServiceRegistry<OrionInstance> serviceRegistry,
            @NotNull AutoServiceRegistrationProperties properties) {
        super(serviceRegistry, properties);
    }

    @Override
    protected Object getConfiguration() {
        return null;
    }

    @Override
    protected boolean isEnabled() {
        return true;
    }

    @Override
    protected @NotNull OrionInstance getRegistration() {
        return new OrionInstance(
                "test-client",
                "localhost",
                8081,
                new HashMap<>()
        );
    }

    @Override
    protected OrionInstance getManagementRegistration() {
        return null;
    }
}
