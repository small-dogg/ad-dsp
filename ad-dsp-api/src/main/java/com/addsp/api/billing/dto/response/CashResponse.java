package com.addsp.api.billing.dto.response;

import com.addsp.domain.billing.entity.Cash;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for cash details.
 */
public record CashResponse(
        Long id,
        Long partnerId,
        Cash.CashType type,
        BigDecimal amount,
        BigDecimal balance,
        LocalDateTime expiredAt,
        LocalDateTime createdAt
) {
    public static CashResponse from(Cash cash) {
        return new CashResponse(
                cash.getId(),
                cash.getPartnerId(),
                cash.getType(),
                cash.getAmount(),
                cash.getBalance(),
                cash.getExpiredAt(),
                cash.getCreatedAt()
        );
    }
}
