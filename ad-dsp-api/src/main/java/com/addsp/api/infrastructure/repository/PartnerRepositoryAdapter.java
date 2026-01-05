package com.addsp.api.infrastructure.repository;

import com.addsp.domain.partner.entity.Partner;
import com.addsp.domain.partner.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adapter implementing domain PartnerRepository using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class PartnerRepositoryAdapter implements PartnerRepository {

    private final PartnerJpaRepository partnerJpaRepository;

    @Override
    public Partner save(Partner partner) {
        return partnerJpaRepository.save(partner);
    }

    @Override
    public Optional<Partner> findById(Long id) {
        return partnerJpaRepository.findById(id);
    }

    @Override
    public Optional<Partner> findByEmail(String email) {
        return partnerJpaRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return partnerJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByBusinessNumber(String businessNumber) {
        return partnerJpaRepository.existsByBusinessNumber(businessNumber);
    }
}
