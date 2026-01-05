package com.addsp.domain.partner.repository;

import com.addsp.domain.partner.entity.Partner;

import java.util.Optional;

public interface PartnerRepository {

    Partner save(Partner partner);

    Optional<Partner> findById(Long id);

    Optional<Partner> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByBusinessNumber(String businessNumber);
}
