package com.addsp.api.outbox.service;

import com.addsp.domain.outbox.entity.OutboxEvent;
import com.addsp.domain.outbox.event.DomainEvent;
import com.addsp.domain.outbox.repository.OutboxEventRepository;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for saving domain events to the outbox table.
 * Events are saved in the same transaction as business logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * Saves a domain event to the outbox table.
     * Must be called within the same transaction as the business logic.
     */
    public void save(DomainEvent event) {
        String payload = serializeToJson(event);

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType(event.getAggregateType())
                .aggregateId(event.getAggregateId())
                .eventType(event.getEventType())
                .payload(payload)
                .build();

        outboxEventRepository.save(outboxEvent);

        log.debug("Saved outbox event: type={}, aggregateType={}, aggregateId={}",
                event.getEventType(), event.getAggregateType(), event.getAggregateId());
    }

    private String serializeToJson(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            log.error("Failed to serialize event: {}", event.getEventType(), e);
            throw new RuntimeException("Failed to serialize domain event", e);
        }
    }
}
