package com.addsp.api.billing.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request DTO for deducting cash.
 */
public record DeductCashRequest(
        @NotNull(message = "차감 금액은 필수입니다.")
        @Positive(message = "차감 금액은 0보다 커야 합니다.")
        BigDecimal amount
) {
}
