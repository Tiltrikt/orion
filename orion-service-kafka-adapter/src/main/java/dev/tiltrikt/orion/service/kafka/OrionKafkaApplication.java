package dev.tiltrikt.orion.service.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.tiltrikt.orion")
public class OrionKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrionKafkaApplication.class, args);
    }
}
