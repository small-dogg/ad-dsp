package com.addsp.domain.outbox.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when Partner cash balance becomes zero.
 */
public record PartnerCashEmptyEvent(
        Long partnerId,
        BigDecimal previousBalance,
        LocalDateTime occurredAt
) implements DomainEvent {

    public static PartnerCashEmptyEvent of(Long partnerId, BigDecimal previousBalance) {
        return new PartnerCashEmptyEvent(partnerId, previousBalance, LocalDateTime.now());
    }

    @Override
    public String getEventType() {
        return "PARTNER_CASH_EMPTY";
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
        return "rtb.partner.cash-empty";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
