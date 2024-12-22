package dev.tiltrikt.orion.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "dev.tiltrikt.orion")
public class OrionApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrionApplication.class, args);
    }
}
