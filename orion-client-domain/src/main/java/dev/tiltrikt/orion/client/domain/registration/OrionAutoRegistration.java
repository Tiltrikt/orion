package dev.tiltrikt.orion.client.domain.registration;

import dev.tiltrikt.orion.client.domain.model.OrionInstance;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrionAutoRegistration extends AbstractAutoServiceRegistration<OrionInstance> {

    @NotNull OrionInstance thisOrionInstance;

    public OrionAutoRegistration(
            @NotNull ServiceRegistry<OrionInstance> serviceRegistry,
            @NotNull AutoServiceRegistrationProperties properties,
            @NotNull OrionInstance thisOrionInstance) {
        super(serviceRegistry, properties);
        this.thisOrionInstance = thisOrionInstance;
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
        return thisOrionInstance;
    }

    @Override
    protected OrionInstance getManagementRegistration() {
        return null;
    }
}