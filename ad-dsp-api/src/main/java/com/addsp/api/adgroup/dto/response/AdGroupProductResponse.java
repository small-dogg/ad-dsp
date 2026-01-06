package com.addsp.api.adgroup.dto.response;

import com.addsp.domain.product.entity.AdGroupProduct;

import java.time.LocalDateTime;

/**
 * 광고그룹-상품 연결 응답.
 */
public record AdGroupProductResponse(
        Long id,
        Long adGroupId,
        Long productId,
        Long partnerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AdGroupProductResponse from(AdGroupProduct adGroupProduct) {
        return new AdGroupProductResponse(
                adGroupProduct.getId(),
                adGroupProduct.getAdGroupId(),
                adGroupProduct.getProductId(),
                adGroupProduct.getPartnerId(),
                adGroupProduct.getCreatedAt(),
                adGroupProduct.getUpdatedAt()
        );
    }
}
