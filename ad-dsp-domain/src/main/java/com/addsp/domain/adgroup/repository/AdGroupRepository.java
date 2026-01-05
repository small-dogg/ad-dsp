package com.addsp.domain.adgroup.repository;

import com.addsp.domain.adgroup.entity.AdGroup;

import java.util.List;
import java.util.Optional;

public interface AdGroupRepository {

    AdGroup save(AdGroup adGroup);

    Optional<AdGroup> findById(Long id);

    List<AdGroup> findByCampaignId(Long campaignId);

    List<AdGroup> findActiveByCampaignId(Long campaignId);
}
