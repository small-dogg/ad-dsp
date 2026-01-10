package com.addsp.api.infrastructure.repository;

import com.addsp.domain.billing.entity.Cash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA repository for Cash entity.
 */
public interface CashJpaRepository extends JpaRepository<Cash, Long> {

    List<Cash> findByPartnerId(Long partnerId);

    List<Cash> findByPartnerIdAndBalanceGreaterThan(Long partnerId, BigDecimal balance);

    /**
     * Find active cash records: balance > 0 and not expired.
     * Ordered by expiration date (nulls last, then earliest first).
     */
    @Query("SELECT c FROM Cash c WHERE c.partnerId = :partnerId " +
            "AND c.balance > 0 " +
            "AND (c.expiredAt IS NULL OR c.expiredAt > CURRENT_TIMESTAMP) " +
            "ORDER BY CASE WHEN c.expiredAt IS NULL THEN 1 ELSE 0 END, c.expiredAt ASC")
    List<Cash> findActiveByPartnerIdOrderByExpiredAtAsc(@Param("partnerId") Long partnerId);
}
