package com.addsp.api.infrastructure.repository;

import com.addsp.common.constant.OutboxStatus;
import com.addsp.domain.outbox.entity.OutboxEvent;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for OutboxEvent entity.
 */
public interface OutboxEventJpaRepository extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status);

    /**
     * Find pending events with pessimistic lock and SKIP LOCKED hint.
     * This allows multiple instances to process events concurrently without blocking.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")})
    @Query("SELECT e FROM OutboxEvent e WHERE e.status = :status ORDER BY e.createdAt ASC LIMIT :limit")
    List<OutboxEvent> findPendingEventsForUpdate(@Param("status") OutboxStatus status, @Param("limit") int limit);
}
