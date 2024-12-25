package dev.tiltrikt.orion.example.runner;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationRunner {

    @NotNull DiscoveryClient discoveryClient;

    @NotNull RestClient restClient;

    @Scheduled(fixedRate = 5000)
    public void makeRemoteCall() {
        try {
            System.out.println(discoveryClient.getServices());
            ServiceInstance serviceInstance = discoveryClient.getInstances("self-registration-client").getFirst();
            String response = restClient.get()
                    .uri(serviceInstance.getUri() + "/helloWorld")
                    .retrieve()
                    .body(String.class);
            System.out.println("Direct request: " + response);
        } catch (Exception e) {
            System.out.println("Startup self-registration-client to see how Orion works with direct requests");
        }

        try {
            String response = restClient.get()
                    .uri("http://localhost:8090/helloWorld")
                    .retrieve()
                    .body(String.class);
            System.out.println("Api gateway: " + response);
        } catch (Exception e) {
            System.out.println("Startup gateway service to see how Orion works with Gateway");
        }
    }
}
