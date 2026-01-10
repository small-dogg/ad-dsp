package com.addsp.api.infrastructure.repository;

import com.addsp.common.constant.AdKeywordStatus;
import com.addsp.domain.keyword.entity.AdKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for AdKeyword entity.
 */
public interface AdKeywordJpaRepository extends JpaRepository<AdKeyword, Long> {

    List<AdKeyword> findByAdGroupId(Long adGroupId);

    List<AdKeyword> findByDealId(Long dealId);

    List<AdKeyword> findByKeywordId(Long keywordId);

    List<AdKeyword> findByAdGroupIdAndDealId(Long adGroupId, Long dealId);

    Optional<AdKeyword> findByAdGroupIdAndDealIdAndKeywordId(Long adGroupId, Long dealId, Long keywordId);

    boolean existsByAdGroupIdAndDealIdAndKeywordId(Long adGroupId, Long dealId, Long keywordId);

    void deleteAllByAdGroupId(Long adGroupId);

    void deleteAllByAdGroupIdAndDealId(Long adGroupId, Long dealId);

    long countByAdGroupId(Long adGroupId);

    long countByAdGroupIdAndStatusNot(Long adGroupId, AdKeywordStatus status);
}
