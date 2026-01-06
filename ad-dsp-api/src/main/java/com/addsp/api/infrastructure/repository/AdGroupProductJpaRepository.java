package com.addsp.api.infrastructure.repository;

import com.addsp.domain.product.entity.AdGroupProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for AdGroupProduct entity.
 */
public interface AdGroupProductJpaRepository extends JpaRepository<AdGroupProduct, Long> {

    List<AdGroupProduct> findByAdGroupId(Long adGroupId);

    List<AdGroupProduct> findByAdGroupIdAndPartnerId(Long adGroupId, Long partnerId);

    Optional<AdGroupProduct> findByAdGroupIdAndProductId(Long adGroupId, Long productId);

    boolean existsByAdGroupIdAndProductId(Long adGroupId, Long productId);

    void deleteByAdGroupIdAndProductId(Long adGroupId, Long productId);

    void deleteAllByAdGroupId(Long adGroupId);
}
