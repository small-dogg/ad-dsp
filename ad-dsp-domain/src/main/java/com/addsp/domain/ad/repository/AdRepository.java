package com.addsp.domain.ad.repository;

import com.addsp.domain.ad.entity.Ad;

import java.util.List;
import java.util.Optional;

public interface AdRepository {

    Ad save(Ad ad);

    Optional<Ad> findById(Long id);

    List<Ad> findByAdGroupId(Long adGroupId);

    List<Ad> findActiveByAdGroupId(Long adGroupId);

    Optional<Ad> findByAdGroupIdAndDealId(Long adGroupId, Long dealId);
}
