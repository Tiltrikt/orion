package dev.tiltrikt.orion.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "Hello world from service registered in Orion!";
    }
}
