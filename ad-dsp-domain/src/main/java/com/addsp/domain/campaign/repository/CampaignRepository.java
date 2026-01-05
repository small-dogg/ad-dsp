package com.addsp.domain.campaign.repository;

import com.addsp.common.constant.CampaignStatus;
import com.addsp.domain.campaign.entity.Campaign;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository {

    Campaign save(Campaign campaign);

    Optional<Campaign> findById(Long id);

    List<Campaign> findByPartnerId(Long partnerId);

    List<Campaign> findByPartnerIdAndStatus(Long partnerId, CampaignStatus status);

    List<Campaign> findActiveByPartnerId(Long partnerId);
}
