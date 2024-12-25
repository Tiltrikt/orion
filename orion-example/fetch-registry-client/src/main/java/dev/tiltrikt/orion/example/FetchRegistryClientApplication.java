package dev.tiltrikt.orion.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "dev.tiltrikt.orion")
public class FetchRegistryClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FetchRegistryClientApplication.class, args);
    }
}
