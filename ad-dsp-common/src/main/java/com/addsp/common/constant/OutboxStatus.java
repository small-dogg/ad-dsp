package com.addsp.common.constant;

/**
 * Outbox event processing status.
 */
public enum OutboxStatus {
    PENDING,     // Waiting to be published
    PROCESSING,  // Currently being processed
    COMPLETED,   // Successfully published to Kafka
    FAILED       // Failed after max retries
}
