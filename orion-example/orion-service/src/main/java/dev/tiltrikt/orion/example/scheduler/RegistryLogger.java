//package dev.tiltrikt.orion.example.scheduler;
//
//import dev.tiltrikt.orion.service.domain.service.InstanceService;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class RegistryLogger {
//
//    @NotNull InstanceService instanceService;
//
//    @Scheduled(fixedRate = 5000)
//    public void log() {
//        System.out.println(instanceService.findAll());
//    }
//}
