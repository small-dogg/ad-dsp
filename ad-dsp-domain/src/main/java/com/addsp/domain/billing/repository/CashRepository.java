package com.addsp.domain.billing.repository;

import com.addsp.domain.billing.entity.Cash;

import java.util.List;
import java.util.Optional;

public interface CashRepository {

    Cash save(Cash cash);

    Optional<Cash> findById(Long id);

    List<Cash> findByPartnerId(Long partnerId);

    List<Cash> findByPartnerIdAndBalanceGreaterThan(Long partnerId, java.math.BigDecimal balance);
}
