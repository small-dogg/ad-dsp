package com.addsp.api.adgroup.dto.response;

import com.addsp.api.infrastructure.client.dto.ProductApiResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 상품 정보 응답 (외부 API 데이터 기반).
 */
public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        String imageUrl,
        Long partnerId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ProductResponse from(ProductApiResponse apiResponse) {
        return new ProductResponse(
                apiResponse.id(),
                apiResponse.name(),
                apiResponse.price(),
                apiResponse.imageUrl(),
                apiResponse.partnerId(),
                apiResponse.createdAt(),
                apiResponse.modifiedAt()
        );
    }
}
