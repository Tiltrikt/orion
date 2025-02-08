package dev.tiltrikt.consul.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ConsulFetchRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsulFetchRegistryApplication.class, args);
    }
}
