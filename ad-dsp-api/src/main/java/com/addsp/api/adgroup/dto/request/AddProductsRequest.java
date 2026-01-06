package com.addsp.api.adgroup.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 광고그룹에 상품 추가 요청.
 */
public record AddProductsRequest(
        @NotEmpty(message = "상품 ID 목록은 필수입니다.")
        List<Long> productIds
) {
}
