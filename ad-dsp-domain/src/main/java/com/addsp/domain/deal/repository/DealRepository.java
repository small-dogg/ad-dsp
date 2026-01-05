package com.addsp.domain.deal.repository;

import com.addsp.domain.deal.entity.Deal;

import java.util.List;
import java.util.Optional;

public interface DealRepository {

    Deal save(Deal deal);

    Optional<Deal> findById(Long id);

    Optional<Deal> findByPartnerIdAndDealId(Long partnerId, Long dealId);

    List<Deal> findByPartnerId(Long partnerId);

    boolean existsByPartnerIdAndDealId(Long partnerId, Long dealId);
}
