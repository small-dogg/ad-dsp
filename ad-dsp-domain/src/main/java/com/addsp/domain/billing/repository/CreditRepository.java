package com.addsp.domain.billing.repository;

import com.addsp.domain.billing.entity.Credit;

import java.util.Optional;

public interface CreditRepository {

    Credit save(Credit credit);

    Optional<Credit> findByPartnerId(Long partnerId);
}
