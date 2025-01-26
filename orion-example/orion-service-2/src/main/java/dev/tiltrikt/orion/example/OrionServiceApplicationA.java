package dev.tiltrikt.orion.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.tiltrikt.orion ")
public class OrionServiceApplicationA {

    public static void main(String[] args) {
        SpringApplication.run(OrionServiceApplicationA.class, args);
    }
}
