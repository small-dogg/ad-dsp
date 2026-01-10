package com.addsp.domain.outbox.event;

import java.time.LocalDateTime;

/**
 * Base interface for all domain events.
 * Events are published via Transactional Outbox Pattern to Kafka.
 */
public interface DomainEvent {

    /**
     * Returns the event type identifier (e.g., "AD_GROUP_STATUS_CHANGED").
     */
    String getEventType();

    /**
     * Returns the aggregate type (e.g., "AdGroup", "AdKeyword", "Cash").
     */
    String getAggregateType();

    /**
     * Returns the aggregate ID.
     */
    Long getAggregateId();

    /**
     * Returns the Kafka topic name for this event.
     */
    String getTopic();

    /**
     * Returns the timestamp when this event occurred.
     */
    LocalDateTime getOccurredAt();
}
