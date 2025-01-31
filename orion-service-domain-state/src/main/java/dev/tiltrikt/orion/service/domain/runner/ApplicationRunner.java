package dev.tiltrikt.orion.service.domain.runner;

import dev.tiltrikt.orion.service.domain.handler.raft.VoteRequestHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationRunner implements CommandLineRunner {

    @NotNull VoteRequestHandler voteRequestHandler;

    @Override
    public void run(String... args) throws Exception {
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                voteRequestHandler.handle(new VoteEvent("all"));
//                voteRequestHandler.handle(new VoteEvent("all"));
//            }
//        };
//        timer.schedule(timerTask, 8000);
    }
}
