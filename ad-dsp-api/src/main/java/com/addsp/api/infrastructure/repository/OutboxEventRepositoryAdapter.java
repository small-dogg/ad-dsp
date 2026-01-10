package com.addsp.api.infrastructure.repository;

import com.addsp.common.constant.OutboxStatus;
import com.addsp.domain.outbox.entity.OutboxEvent;
import com.addsp.domain.outbox.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Adapter implementing domain OutboxEventRepository using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class OutboxEventRepositoryAdapter implements OutboxEventRepository {

    private final OutboxEventJpaRepository outboxEventJpaRepository;

    @Override
    public OutboxEvent save(OutboxEvent outboxEvent) {
        return outboxEventJpaRepository.save(outboxEvent);
    }

    @Override
    public List<OutboxEvent> saveAll(List<OutboxEvent> outboxEvents) {
        return outboxEventJpaRepository.saveAll(outboxEvents);
    }

    @Override
    public List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status, int limit) {
        return outboxEventJpaRepository.findByStatusOrderByCreatedAtAsc(status).stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<OutboxEvent> findPendingEventsForUpdate(int limit) {
        return outboxEventJpaRepository.findPendingEventsForUpdate(OutboxStatus.PENDING, limit);
    }
}
