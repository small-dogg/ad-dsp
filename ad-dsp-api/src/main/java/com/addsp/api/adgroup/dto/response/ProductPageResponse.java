package com.addsp.api.adgroup.dto.response;

import com.addsp.api.infrastructure.client.dto.ProductPageApiResponse;

import java.util.List;

/**
 * 상품 목록 페이징 응답.
 */
public record ProductPageResponse(
        List<ProductResponse> content,
        int page,
        int limit,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
    public static ProductPageResponse from(ProductPageApiResponse apiResponse) {
        List<ProductResponse> content = apiResponse.items().stream()
                .map(ProductResponse::from)
                .toList();
        return new ProductPageResponse(
                content,
                apiResponse.page().page(),
                apiResponse.page().limit(),
                apiResponse.page().totalElements(),
                apiResponse.page().totalPages(),
                apiResponse.page().hasNext()
        );
    }
}
