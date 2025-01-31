//package dev.tiltrikt.orion.service.domain.publisher;
//
//import dev.tiltrikt.orion.service.common.publisher.LeaderHeartbeatPublisher;
//import dev.tiltrikt.orion.service.domain.event.LeaderHeartbeatEvent;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class LeaderHeartbeatPublisherImpl implements LeaderHeartbeatPublisher {
//
//    @Override
//    public void publish(@NotNull LeaderHeartbeatEvent event) {
//        System.out.println("Publishing event: " + event);
//    }
//}
