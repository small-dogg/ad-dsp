package com.addsp.api.billing.dto.request;

import com.addsp.domain.billing.entity.Cash;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO for charging cash.
 */
public record ChargeCashRequest(
        @NotNull(message = "캐시 타입은 필수입니다.")
        Cash.CashType type,

        @NotNull(message = "충전 금액은 필수입니다.")
        @Positive(message = "충전 금액은 0보다 커야 합니다.")
        BigDecimal amount,

        LocalDateTime expiredAt
) {
}
