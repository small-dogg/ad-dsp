package com.addsp.domain.outbox.event;

import com.addsp.domain.billing.entity.Cash;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when Partner cash is charged.
 */
public record PartnerCashChargedEvent(
        Long partnerId,
        Long cashId,
        Cash.CashType cashType,
        BigDecimal chargedAmount,
        BigDecimal totalBalance,
        LocalDateTime occurredAt
) implements DomainEvent {

    public static PartnerCashChargedEvent of(Long partnerId, Long cashId, Cash.CashType cashType,
                                              BigDecimal chargedAmount, BigDecimal totalBalance) {
        return new PartnerCashChargedEvent(partnerId, cashId, cashType, chargedAmount, totalBalance, LocalDateTime.now());
    }

    @Override
    public String getEventType() {
        return "PARTNER_CASH_CHARGED";
    }

    @Override
    public String getAggregateType() {
        return "Partner";
    }

    @Override
    public Long getAggregateId() {
        return partnerId;
    }

    @Override
    public String getTopic() {
        return "rtb.partner.cash-charged";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
