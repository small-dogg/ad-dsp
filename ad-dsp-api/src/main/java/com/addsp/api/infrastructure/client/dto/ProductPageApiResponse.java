package com.addsp.api.infrastructure.client.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Paginated product list from external Product API.
 */
public record ProductPageApiResponse(
        List<ProductItem> items,
        PageMeta page
) {
    public record ProductItem(
            Long id,
            Long partnerId,
            String name,
            String status,
            Long price,
            String imageUrl,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
    }

    public record PageMeta(
            int page,
            int limit,
            long totalElements,
            int totalPages,
            boolean hasNext
    ) {
    }
}
