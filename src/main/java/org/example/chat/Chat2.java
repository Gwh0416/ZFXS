package org.example.chat;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequestMapping("/events")
public class Chat2 {

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Async
//    @GetMapping("/sse")
    public SseEmitter handleSse(HttpServletResponse response) {
        SseEmitter emitter = new SseEmitter();
        response.setContentType("text/event-stream");
        Flux.interval(Duration.ofSeconds(1))
                .map(i -> "Server-Sent Event #" + i)
                .doOnCancel(() -> emitter.complete())
                .subscribe(
                        data -> {
                            try {
                                emitter.send(data);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        error -> emitter.completeWithError(error),
                        () -> emitter.complete()
                );
        System.out.println(response.getContentType());

        return emitter;
    }

}
