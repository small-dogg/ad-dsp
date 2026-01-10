package com.addsp.api.infrastructure.kafka;

import com.addsp.domain.outbox.entity.OutboxEvent;
import com.addsp.domain.outbox.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Scheduler that polls outbox table and publishes events to Kafka.
 * Uses pessimistic locking for distributed safety.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaEventPublisher kafkaEventPublisher;

    @Value("${outbox.publisher.batch-size:100}")
    private int batchSize;

    @Value("${outbox.publisher.max-retry-count:5}")
    private int maxRetryCount;

    /**
     * Polls pending events and publishes to Kafka.
     * Runs every second (configured in application.yml).
     */
    @Scheduled(fixedRateString = "${outbox.publisher.poll-interval-ms:1000}")
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxEventRepository.findPendingEventsForUpdate(batchSize);

        if (events.isEmpty()) {
            return;
        }

        log.debug("Found {} pending outbox events to publish", events.size());

        for (OutboxEvent event : events) {
            processEvent(event);
        }
    }

    private void processEvent(OutboxEvent event) {
        event.markAsProcessing();

        try {
            CompletableFuture<?> future = kafkaEventPublisher.send(event);
            future.get(); // Wait for completion

            event.markAsCompleted();
            log.info("Published outbox event: id={}, type={}, aggregateId={}",
                    event.getId(), event.getEventType(), event.getAggregateId());

        } catch (Exception e) {
            log.error("Failed to publish outbox event: id={}, type={}, error={}",
                    event.getId(), event.getEventType(), e.getMessage());

            event.incrementRetryCount();

            if (event.hasExceededMaxRetries(maxRetryCount)) {
                event.markAsFailed();
                log.error("Outbox event exceeded max retries, marking as FAILED: id={}", event.getId());
            } else {
                event.resetToPending();
            }
        }
    }
}
