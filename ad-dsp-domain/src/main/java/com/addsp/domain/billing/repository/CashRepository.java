package com.addsp.domain.billing.repository;

import com.addsp.domain.billing.entity.Cash;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Cash entity.
 */
public interface CashRepository {

    Cash save(Cash cash);

    Optional<Cash> findById(Long id);

    List<Cash> findByPartnerId(Long partnerId);

    List<Cash> findByPartnerIdAndBalanceGreaterThan(Long partnerId, java.math.BigDecimal balance);

    /**
     * Find active cash records ordered by expiration date (earliest first).
     * Active means: balance > 0 and (expiredAt is null or expiredAt > now)
     */
    List<Cash> findActiveByPartnerIdOrderByExpiredAtAsc(Long partnerId);
}
