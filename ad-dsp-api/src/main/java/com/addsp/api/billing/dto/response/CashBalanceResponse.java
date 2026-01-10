package com.addsp.api.billing.dto.response;

import java.math.BigDecimal;

/**
 * Response DTO for cash balance.
 */
public record CashBalanceResponse(
        Long partnerId,
        BigDecimal totalBalance
) {
    public static CashBalanceResponse of(Long partnerId, BigDecimal totalBalance) {
        return new CashBalanceResponse(partnerId, totalBalance);
    }
}
