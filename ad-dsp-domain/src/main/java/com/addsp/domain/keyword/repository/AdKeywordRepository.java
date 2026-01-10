package com.addsp.domain.keyword.repository;

import com.addsp.domain.keyword.entity.AdKeyword;

import java.util.List;
import java.util.Optional;

public interface AdKeywordRepository {

    AdKeyword save(AdKeyword adKeyword);

    Optional<AdKeyword> findById(Long id);

    List<AdKeyword> findByAdGroupId(Long adGroupId);

    List<AdKeyword> findByDealId(Long dealId);

    List<AdKeyword> findByKeywordId(Long keywordId);

    List<AdKeyword> findByAdGroupIdAndDealId(Long adGroupId, Long dealId);

    Optional<AdKeyword> findByAdGroupIdAndDealIdAndKeywordId(Long adGroupId, Long dealId, Long keywordId);

    boolean existsByAdGroupIdAndDealIdAndKeywordId(Long adGroupId, Long dealId, Long keywordId);

    void deleteById(Long id);

    void deleteAllByAdGroupId(Long adGroupId);

    void deleteAllByAdGroupIdAndDealId(Long adGroupId, Long dealId);

    long countByAdGroupId(Long adGroupId);

    long countByAdGroupIdAndStatusNot(Long adGroupId, com.addsp.common.constant.AdKeywordStatus status);
}
