package com.addsp.domain.outbox.repository;

import com.addsp.common.constant.OutboxStatus;
import com.addsp.domain.outbox.entity.OutboxEvent;

import java.util.List;

/**
 * Repository interface for OutboxEvent.
 */
public interface OutboxEventRepository {

    OutboxEvent save(OutboxEvent outboxEvent);

    List<OutboxEvent> saveAll(List<OutboxEvent> outboxEvents);

    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status, int limit);

    /**
     * Find pending events with pessimistic lock (FOR UPDATE SKIP LOCKED).
     * Used for distributed-safe polling.
     */
    List<OutboxEvent> findPendingEventsForUpdate(int limit);
}
