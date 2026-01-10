package com.addsp.api.infrastructure.repository;

import com.addsp.domain.billing.entity.Cash;
import com.addsp.domain.billing.repository.CashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing domain CashRepository using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class CashRepositoryAdapter implements CashRepository {

    private final CashJpaRepository cashJpaRepository;

    @Override
    public Cash save(Cash cash) {
        return cashJpaRepository.save(cash);
    }

    @Override
    public Optional<Cash> findById(Long id) {
        return cashJpaRepository.findById(id);
    }

    @Override
    public List<Cash> findByPartnerId(Long partnerId) {
        return cashJpaRepository.findByPartnerId(partnerId);
    }

    @Override
    public List<Cash> findByPartnerIdAndBalanceGreaterThan(Long partnerId, BigDecimal balance) {
        return cashJpaRepository.findByPartnerIdAndBalanceGreaterThan(partnerId, balance);
    }

    @Override
    public List<Cash> findActiveByPartnerIdOrderByExpiredAtAsc(Long partnerId) {
        return cashJpaRepository.findActiveByPartnerIdOrderByExpiredAtAsc(partnerId);
    }
}
