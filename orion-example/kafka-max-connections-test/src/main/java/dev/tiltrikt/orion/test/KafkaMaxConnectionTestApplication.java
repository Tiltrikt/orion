package dev.tiltrikt.orion.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaMaxConnectionTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaMaxConnectionTestApplication.class, args);
    }
}
