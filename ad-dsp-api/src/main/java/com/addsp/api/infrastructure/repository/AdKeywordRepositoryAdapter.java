package com.addsp.api.infrastructure.repository;

import com.addsp.domain.keyword.entity.AdKeyword;
import com.addsp.domain.keyword.repository.AdKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing domain AdKeywordRepository using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class AdKeywordRepositoryAdapter implements AdKeywordRepository {

    private final AdKeywordJpaRepository adKeywordJpaRepository;

    @Override
    public AdKeyword save(AdKeyword adKeyword) {
        return adKeywordJpaRepository.save(adKeyword);
    }

    @Override
    public Optional<AdKeyword> findById(Long id) {
        return adKeywordJpaRepository.findById(id);
    }

    @Override
    public List<AdKeyword> findByAdGroupId(Long adGroupId) {
        return adKeywordJpaRepository.findByAdGroupId(adGroupId);
    }

    @Override
    public List<AdKeyword> findByDealId(Long dealId) {
        return adKeywordJpaRepository.findByDealId(dealId);
    }

    @Override
    public List<AdKeyword> findByKeywordId(Long keywordId) {
        return adKeywordJpaRepository.findByKeywordId(keywordId);
    }

    @Override
    public List<AdKeyword> findByAdGroupIdAndDealId(Long adGroupId, Long dealId) {
        return adKeywordJpaRepository.findByAdGroupIdAndDealId(adGroupId, dealId);
    }

    @Override
    public Optional<AdKeyword> findByAdGroupIdAndDealIdAndKeywordId(Long adGroupId, Long dealId, Long keywordId) {
        return adKeywordJpaRepository.findByAdGroupIdAndDealIdAndKeywordId(adGroupId, dealId, keywordId);
    }

    @Override
    public boolean existsByAdGroupIdAndDealIdAndKeywordId(Long adGroupId, Long dealId, Long keywordId) {
        return adKeywordJpaRepository.existsByAdGroupIdAndDealIdAndKeywordId(adGroupId, dealId, keywordId);
    }

    @Override
    public void deleteById(Long id) {
        adKeywordJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllByAdGroupId(Long adGroupId) {
        adKeywordJpaRepository.deleteAllByAdGroupId(adGroupId);
    }

    @Override
    public void deleteAllByAdGroupIdAndDealId(Long adGroupId, Long dealId) {
        adKeywordJpaRepository.deleteAllByAdGroupIdAndDealId(adGroupId, dealId);
    }
}
