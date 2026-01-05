package com.addsp.api.infrastructure.repository;

import com.addsp.domain.partner.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for Partner entity.
 */
public interface PartnerJpaRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByBusinessNumber(String businessNumber);
}
