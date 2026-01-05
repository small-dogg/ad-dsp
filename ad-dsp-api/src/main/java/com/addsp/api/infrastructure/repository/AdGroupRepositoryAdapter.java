package com.addsp.api.infrastructure.repository;

import com.addsp.common.constant.AdStatus;
import com.addsp.domain.adgroup.entity.AdGroup;
import com.addsp.domain.adgroup.repository.AdGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing domain AdGroupRepository using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class AdGroupRepositoryAdapter implements AdGroupRepository {

    private final AdGroupJpaRepository adGroupJpaRepository;

    @Override
    public AdGroup save(AdGroup adGroup) {
        return adGroupJpaRepository.save(adGroup);
    }

    @Override
    public Optional<AdGroup> findById(Long id) {
        return adGroupJpaRepository.findById(id);
    }

    @Override
    public List<AdGroup> findByPartnerId(Long partnerId) {
        return adGroupJpaRepository.findByPartnerId(partnerId);
    }

    @Override
    public List<AdGroup> findByPartnerIdAndStatus(Long partnerId, AdStatus status) {
        return adGroupJpaRepository.findByPartnerIdAndStatus(partnerId, status);
    }

    @Override
    public List<AdGroup> findActiveByPartnerId(Long partnerId) {
        return adGroupJpaRepository.findActiveByPartnerId(partnerId);
    }

    @Override
    public boolean existsByPartnerIdAndName(Long partnerId, String name) {
        return adGroupJpaRepository.existsByPartnerIdAndName(partnerId, name);
    }

    @Override
    public boolean existsByPartnerIdAndNameAndIdNot(Long partnerId, String name, Long id) {
        return adGroupJpaRepository.existsByPartnerIdAndNameAndIdNot(partnerId, name, id);
    }

    @Override
    public void delete(AdGroup adGroup) {
        adGroupJpaRepository.delete(adGroup);
    }
}
