package com.addsp.api.infrastructure.repository;

import com.addsp.domain.product.entity.AdGroupProduct;
import com.addsp.domain.product.repository.AdGroupProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing domain AdGroupProductRepository using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class AdGroupProductRepositoryAdapter implements AdGroupProductRepository {

    private final AdGroupProductJpaRepository adGroupProductJpaRepository;

    @Override
    public AdGroupProduct save(AdGroupProduct adGroupProduct) {
        return adGroupProductJpaRepository.save(adGroupProduct);
    }

    @Override
    public Optional<AdGroupProduct> findById(Long id) {
        return adGroupProductJpaRepository.findById(id);
    }

    @Override
    public List<AdGroupProduct> findByAdGroupId(Long adGroupId) {
        return adGroupProductJpaRepository.findByAdGroupId(adGroupId);
    }

    @Override
    public List<AdGroupProduct> findByAdGroupIdAndPartnerId(Long adGroupId, Long partnerId) {
        return adGroupProductJpaRepository.findByAdGroupIdAndPartnerId(adGroupId, partnerId);
    }

    @Override
    public Optional<AdGroupProduct> findByAdGroupIdAndProductId(Long adGroupId, Long productId) {
        return adGroupProductJpaRepository.findByAdGroupIdAndProductId(adGroupId, productId);
    }

    @Override
    public boolean existsByAdGroupIdAndProductId(Long adGroupId, Long productId) {
        return adGroupProductJpaRepository.existsByAdGroupIdAndProductId(adGroupId, productId);
    }

    @Override
    public void deleteById(Long id) {
        adGroupProductJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByAdGroupIdAndProductId(Long adGroupId, Long productId) {
        adGroupProductJpaRepository.deleteByAdGroupIdAndProductId(adGroupId, productId);
    }

    @Override
    public void deleteAllByAdGroupId(Long adGroupId) {
        adGroupProductJpaRepository.deleteAllByAdGroupId(adGroupId);
    }
}
