package com.addsp.domain.product.repository;

import com.addsp.domain.product.entity.AdGroupProduct;

import java.util.List;
import java.util.Optional;

public interface AdGroupProductRepository {

    AdGroupProduct save(AdGroupProduct adGroupProduct);

    Optional<AdGroupProduct> findById(Long id);

    List<AdGroupProduct> findByAdGroupId(Long adGroupId);

    List<AdGroupProduct> findByAdGroupIdAndPartnerId(Long adGroupId, Long partnerId);

    Optional<AdGroupProduct> findByAdGroupIdAndProductId(Long adGroupId, Long productId);

    boolean existsByAdGroupIdAndProductId(Long adGroupId, Long productId);

    void deleteById(Long id);

    void deleteByAdGroupIdAndProductId(Long adGroupId, Long productId);

    void deleteAllByAdGroupId(Long adGroupId);
}
