package com.addsp.api.infrastructure.client.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product data from external Product API.
 */
public record ProductApiResponse(
        Long id,
        String name,
        Long price,
        String imageUrl,
        Long partnerId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}

