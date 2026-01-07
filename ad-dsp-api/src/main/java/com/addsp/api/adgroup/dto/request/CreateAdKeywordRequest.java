package com.addsp.api.adgroup.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * 광고 키워드 생성 요청.
 */
public record CreateAdKeywordRequest(
        @NotNull(message = "상품 ID는 필수입니다.")
        Long dealId,

        @NotBlank(message = "키워드는 필수입니다.")
        String keyword,

        @NotNull(message = "입찰가는 필수입니다.")
        @DecimalMin(value = "150", message = "입찰가는 150원 이상이어야 합니다.")
        @DecimalMax(value = "99000", message = "입찰가는 99,000원 이하여야 합니다.")
        BigDecimal bidAmount
) {
}
