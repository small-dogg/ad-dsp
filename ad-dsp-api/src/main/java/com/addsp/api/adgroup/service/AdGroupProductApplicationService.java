package com.addsp.api.adgroup.service;

import com.addsp.api.adgroup.dto.request.AddProductsRequest;
import com.addsp.api.adgroup.dto.response.AdGroupProductResponse;
import com.addsp.api.adgroup.dto.response.ProductPageResponse;
import com.addsp.api.adgroup.dto.response.ProductResponse;
import com.addsp.api.infrastructure.client.ProductApiClient;
import com.addsp.api.infrastructure.client.dto.ProductApiResponse;
import com.addsp.api.infrastructure.client.dto.ProductPageApiResponse;
import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.adgroup.service.AdGroupService;
import com.addsp.domain.keyword.service.AdKeywordService;
import com.addsp.domain.product.entity.AdGroupProduct;
import com.addsp.domain.product.repository.AdGroupProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 광고그룹-상품 연결 애플리케이션 서비스.
 * 광고그룹과 상품의 연결을 관리하고, 외부 상품 API를 프록시한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdGroupProductApplicationService {

    private final AdGroupProductRepository adGroupProductRepository;
    private final AdGroupService adGroupService;
    private final ProductApiClient productApiClient;
    private final AdKeywordService adKeywordService;

    /**
     * 광고그룹에 상품 추가.
     */
    @Transactional
    public List<AdGroupProductResponse> addProducts(Long partnerId, Long adGroupId, AddProductsRequest request) {
        // Verify ad group ownership
        adGroupService.findByIdAndPartnerId(adGroupId, partnerId);

        List<AdGroupProductResponse> responses = new ArrayList<>();

        for (Long productId : request.productIds()) {
            // Check if product already exists in ad group
            if (adGroupProductRepository.existsByAdGroupIdAndProductId(adGroupId, productId)) {
                throw new BusinessException(ErrorCode.PRODUCT_ALREADY_EXISTS);
            }

            // Verify product exists via external API
            productApiClient.getProduct(productId);

            AdGroupProduct adGroupProduct = AdGroupProduct.builder()
                    .adGroupId(adGroupId)
                    .productId(productId)
                    .partnerId(partnerId)
                    .build();

            AdGroupProduct saved = adGroupProductRepository.save(adGroupProduct);
            responses.add(AdGroupProductResponse.from(saved));
        }

        return responses;
    }

    /**
     * 광고그룹의 상품 목록 조회.
     */
    public List<AdGroupProductResponse> findByAdGroupId(Long partnerId, Long adGroupId) {
        // Verify ad group ownership
        adGroupService.findByIdAndPartnerId(adGroupId, partnerId);

        return adGroupProductRepository.findByAdGroupIdAndPartnerId(adGroupId, partnerId).stream()
                .map(AdGroupProductResponse::from)
                .toList();
    }

    /**
     * 광고그룹에서 상품 삭제.
     * 상품과 연결된 광고 키워드도 함께 삭제된다 (cascade).
     */
    @Transactional
    public void removeProduct(Long partnerId, Long adGroupId, Long productId) {
        // Verify ad group ownership
        adGroupService.findByIdAndPartnerId(adGroupId, partnerId);

        AdGroupProduct adGroupProduct = adGroupProductRepository
                .findByAdGroupIdAndProductId(adGroupId, productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        // Verify ownership
        if (!adGroupProduct.getPartnerId().equals(partnerId)) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // Cascade delete: remove all ad keywords linked to this product in this ad group
        adKeywordService.deleteAllByAdGroupIdAndDealId(adGroupId, productId);

        adGroupProductRepository.deleteByAdGroupIdAndProductId(adGroupId, productId);
    }

    /**
     * 외부 상품 API에서 상품 목록 조회 (프록시).
     */
    public ProductPageResponse getProducts(Long partnerId, int page, int limit,
                                           String searchKeyword, String searchType, String sortType) {
        ProductPageApiResponse apiResponse = productApiClient.getProducts(
                page, limit, partnerId, searchKeyword, searchType, sortType);
        return ProductPageResponse.from(apiResponse);
    }

    /**
     * 외부 상품 API에서 단일 상품 조회 (프록시).
     */
    public ProductResponse getProduct(Long productId) {
        ProductApiResponse apiResponse = productApiClient.getProduct(productId);
        return ProductResponse.from(apiResponse);
    }
}
