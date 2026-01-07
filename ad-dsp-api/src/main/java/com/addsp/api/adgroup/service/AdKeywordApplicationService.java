package com.addsp.api.adgroup.service;

import com.addsp.api.adgroup.dto.request.CreateAdKeywordRequest;
import com.addsp.api.adgroup.dto.request.UpdateAdKeywordBidRequest;
import com.addsp.api.adgroup.dto.response.AdKeywordResponse;
import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.adgroup.service.AdGroupService;
import com.addsp.domain.keyword.entity.AdKeyword;
import com.addsp.domain.keyword.entity.Keyword;
import com.addsp.domain.keyword.service.AdKeywordService;
import com.addsp.domain.keyword.service.KeywordService;
import com.addsp.domain.product.repository.AdGroupProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 광고 키워드 애플리케이션 서비스.
 * 광고그룹 내에서 상품(Deal)과 키워드를 연결하고 입찰가를 관리한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdKeywordApplicationService {

    private final AdKeywordService adKeywordService;
    private final KeywordService keywordService;
    private final AdGroupService adGroupService;
    private final AdGroupProductRepository adGroupProductRepository;

    /**
     * 광고그룹에 광고 키워드 추가.
     */
    @Transactional
    public AdKeywordResponse create(Long partnerId, Long adGroupId, CreateAdKeywordRequest request) {
        // Verify ad group ownership
        adGroupService.findByIdAndPartnerId(adGroupId, partnerId);

        // Verify deal is in the ad group
        validateDealInAdGroup(adGroupId, request.dealId());

        AdKeyword adKeyword = adKeywordService.create(
                adGroupId,
                request.dealId(),
                request.keyword(),
                request.bidAmount()
        );

        Keyword keyword = keywordService.findById(adKeyword.getKeywordId());
        return AdKeywordResponse.from(adKeyword, keyword.getKeyword());
    }

    /**
     * 광고그룹의 광고 키워드 목록 조회.
     */
    public List<AdKeywordResponse> findByAdGroupId(Long partnerId, Long adGroupId) {
        // Verify ad group ownership
        adGroupService.findByIdAndPartnerId(adGroupId, partnerId);

        return adKeywordService.findByAdGroupId(adGroupId).stream()
                .map(adKeyword -> {
                    Keyword keyword = keywordService.findById(adKeyword.getKeywordId());
                    return AdKeywordResponse.from(adKeyword, keyword.getKeyword());
                })
                .toList();
    }

    /**
     * 광고그룹의 특정 상품에 대한 광고 키워드 목록 조회.
     */
    public List<AdKeywordResponse> findByAdGroupIdAndDealId(Long partnerId, Long adGroupId, Long dealId) {
        // Verify ad group ownership
        adGroupService.findByIdAndPartnerId(adGroupId, partnerId);

        return adKeywordService.findByAdGroupIdAndDealId(adGroupId, dealId).stream()
                .map(adKeyword -> {
                    Keyword keyword = keywordService.findById(adKeyword.getKeywordId());
                    return AdKeywordResponse.from(adKeyword, keyword.getKeyword());
                })
                .toList();
    }

    /**
     * 광고 키워드 입찰가 수정.
     */
    @Transactional
    public AdKeywordResponse updateBidAmount(Long partnerId, Long adGroupId, Long adKeywordId,
                                              UpdateAdKeywordBidRequest request) {
        // Verify ad group ownership
        adGroupService.findByIdAndPartnerId(adGroupId, partnerId);

        AdKeyword adKeyword = adKeywordService.findById(adKeywordId);

        // Verify ad keyword belongs to the ad group
        if (!adKeyword.getAdGroupId().equals(adGroupId)) {
            throw new BusinessException(ErrorCode.AD_KEYWORD_NOT_FOUND);
        }

        adKeyword = adKeywordService.updateBidAmount(adKeywordId, request.bidAmount());

        Keyword keyword = keywordService.findById(adKeyword.getKeywordId());
        return AdKeywordResponse.from(adKeyword, keyword.getKeyword());
    }

    /**
     * 광고 키워드 삭제.
     */
    @Transactional
    public void delete(Long partnerId, Long adGroupId, Long adKeywordId) {
        // Verify ad group ownership
        adGroupService.findByIdAndPartnerId(adGroupId, partnerId);

        AdKeyword adKeyword = adKeywordService.findById(adKeywordId);

        // Verify ad keyword belongs to the ad group
        if (!adKeyword.getAdGroupId().equals(adGroupId)) {
            throw new BusinessException(ErrorCode.AD_KEYWORD_NOT_FOUND);
        }

        adKeywordService.delete(adKeywordId);
    }

    /**
     * 광고그룹에서 상품 삭제 시 관련 광고 키워드 삭제 (cascade).
     */
    @Transactional
    public void deleteAllByAdGroupIdAndDealId(Long adGroupId, Long dealId) {
        adKeywordService.deleteAllByAdGroupIdAndDealId(adGroupId, dealId);
    }

    private void validateDealInAdGroup(Long adGroupId, Long dealId) {
        if (!adGroupProductRepository.existsByAdGroupIdAndProductId(adGroupId, dealId)) {
            throw new BusinessException(ErrorCode.AD_KEYWORD_DEAL_NOT_IN_AD_GROUP);
        }
    }
}
