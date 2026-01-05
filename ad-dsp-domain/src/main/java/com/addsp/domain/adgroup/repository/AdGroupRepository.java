package com.addsp.domain.adgroup.repository;

import com.addsp.common.constant.AdStatus;
import com.addsp.domain.adgroup.entity.AdGroup;

import java.util.List;
import java.util.Optional;

public interface AdGroupRepository {

    AdGroup save(AdGroup adGroup);

    Optional<AdGroup> findById(Long id);

    List<AdGroup> findByPartnerId(Long partnerId);

    List<AdGroup> findByPartnerIdAndStatus(Long partnerId, AdStatus status);

    List<AdGroup> findActiveByPartnerId(Long partnerId);
}
