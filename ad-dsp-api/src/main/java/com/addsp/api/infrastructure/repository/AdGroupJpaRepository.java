package com.addsp.api.infrastructure.repository;

import com.addsp.common.constant.AdStatus;
import com.addsp.domain.adgroup.entity.AdGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for AdGroup entity.
 */
public interface AdGroupJpaRepository extends JpaRepository<AdGroup, Long> {

    List<AdGroup> findByPartnerId(Long partnerId);

    List<AdGroup> findByPartnerIdAndStatus(Long partnerId, AdStatus status);

    @Query("SELECT ag FROM AdGroup ag WHERE ag.partnerId = :partnerId AND ag.status = 'ACTIVE'")
    List<AdGroup> findActiveByPartnerId(@Param("partnerId") Long partnerId);

    boolean existsByPartnerIdAndName(Long partnerId, String name);

    boolean existsByPartnerIdAndNameAndIdNot(Long partnerId, String name, Long id);
}
