package dev.tiltrikt.orion.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.tiltrikt.orion")
public class SelfRegistrationClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelfRegistrationClientApplication.class, args);
    }
}
