package com.appsdev.estore.product.command.controller;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("management")
@Slf4j
@RequiredArgsConstructor
public class EventsReplayController {

    private final EventProcessingConfiguration eventProcessingConfiguration;

    @PostMapping("/event-processor/{processorName}/reset")
    public ResponseEntity<String> replayEvents(@PathVariable String processorName) {
        log.info("Replay events for {}", processorName);
        Optional<TrackingEventProcessor> trackingEvent = eventProcessingConfiguration.eventProcessor(
                processorName, TrackingEventProcessor.class);
        if (trackingEvent.isPresent()) {
            TrackingEventProcessor trackingEventProcessor = trackingEvent.get();
            trackingEventProcessor.shutDown();
            trackingEventProcessor.resetTokens();
            trackingEventProcessor.start();
            return ResponseEntity.ok()
                    .body(String.format("The event processor with a name [%s] has been reset", processorName));
        } else {
            return ResponseEntity.badRequest()
                    .body(String.format("The event processor with a name [%s] has not been found", processorName));
        }
    }
}
