package com.addsp.api.infrastructure.client.dto;

import java.util.List;

/**
 * Paginated product list from external Product API.
 */
public record ProductPageApiResponse(
        List<ProductApiResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}
