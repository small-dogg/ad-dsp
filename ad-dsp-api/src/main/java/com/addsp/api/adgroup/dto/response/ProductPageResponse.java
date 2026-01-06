package com.addsp.api.adgroup.dto.response;

import com.addsp.api.infrastructure.client.dto.ProductPageApiResponse;

import java.util.List;

/**
 * 상품 목록 페이징 응답.
 */
public record ProductPageResponse(
        List<ProductResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static ProductPageResponse from(ProductPageApiResponse apiResponse) {
        List<ProductResponse> content = apiResponse.content().stream()
                .map(ProductResponse::from)
                .toList();
        return new ProductPageResponse(
                content,
                apiResponse.page(),
                apiResponse.size(),
                apiResponse.totalElements(),
                apiResponse.totalPages(),
                apiResponse.first(),
                apiResponse.last()
        );
    }
}
