package com.addsp.domain.keyword.service;

import com.addsp.common.constant.AdKeywordStatus;
import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.keyword.entity.AdKeyword;
import com.addsp.domain.keyword.entity.Keyword;
import com.addsp.domain.keyword.repository.AdKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 광고 키워드 도메인 서비스.
 * 광고그룹 내에서 상품(Deal)과 키워드를 연결하고 입찰가를 관리한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdKeywordService {

    private final AdKeywordRepository adKeywordRepository;
    private final KeywordService keywordService;

    @Transactional
    public AdKeyword create(Long adGroupId, Long dealId, String keywordText, BigDecimal bidAmount) {
        validateBidAmount(bidAmount);

        // Get or create the Keyword entity
        Keyword keyword = keywordService.findOrCreate(keywordText);

        // Check if AdKeyword already exists
        if (adKeywordRepository.existsByAdGroupIdAndDealIdAndKeywordId(adGroupId, dealId, keyword.getId())) {
            throw new BusinessException(ErrorCode.AD_KEYWORD_ALREADY_EXISTS);
        }

        AdKeyword adKeyword = AdKeyword.builder()
                .adGroupId(adGroupId)
                .dealId(dealId)
                .keywordId(keyword.getId())
                .status(AdKeywordStatus.PENDING)
                .bidAmount(bidAmount)
                .build();

        return adKeywordRepository.save(adKeyword);
    }

    public AdKeyword findById(Long id) {
        return adKeywordRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.AD_KEYWORD_NOT_FOUND));
    }

    public List<AdKeyword> findByAdGroupId(Long adGroupId) {
        return adKeywordRepository.findByAdGroupId(adGroupId);
    }

    public List<AdKeyword> findByAdGroupIdAndDealId(Long adGroupId, Long dealId) {
        return adKeywordRepository.findByAdGroupIdAndDealId(adGroupId, dealId);
    }

    @Transactional
    public AdKeyword updateBidAmount(Long id, BigDecimal bidAmount) {
        validateBidAmount(bidAmount);

        AdKeyword adKeyword = findById(id);
        adKeyword.updateBidAmount(bidAmount);
        return adKeyword;
    }

    @Transactional
    public AdKeyword approve(Long id) {
        AdKeyword adKeyword = findById(id);
        adKeyword.approve();
        return adKeyword;
    }

    @Transactional
    public AdKeyword reject(Long id) {
        AdKeyword adKeyword = findById(id);
        adKeyword.reject();
        return adKeyword;
    }

    @Transactional
    public AdKeyword deactivate(Long id) {
        AdKeyword adKeyword = findById(id);
        adKeyword.deactivate();
        return adKeyword;
    }

    @Transactional
    public void delete(Long id) {
        AdKeyword adKeyword = findById(id);
        adKeywordRepository.deleteById(adKeyword.getId());
    }

    @Transactional
    public void deleteAllByAdGroupId(Long adGroupId) {
        adKeywordRepository.deleteAllByAdGroupId(adGroupId);
    }

    @Transactional
    public void deleteAllByAdGroupIdAndDealId(Long adGroupId, Long dealId) {
        adKeywordRepository.deleteAllByAdGroupIdAndDealId(adGroupId, dealId);
    }

    private void validateBidAmount(BigDecimal bidAmount) {
        if (bidAmount == null ||
            bidAmount.compareTo(AdKeyword.MIN_BID_AMOUNT) < 0 ||
            bidAmount.compareTo(AdKeyword.MAX_BID_AMOUNT) > 0) {
            throw new BusinessException(ErrorCode.AD_KEYWORD_INVALID_BID_AMOUNT);
        }
    }
}
