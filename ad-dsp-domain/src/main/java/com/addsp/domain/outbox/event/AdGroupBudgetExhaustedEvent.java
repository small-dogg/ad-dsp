package com.addsp.domain.outbox.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when AdGroup daily budget is exhausted during billing.
 */
public record AdGroupBudgetExhaustedEvent(
        Long adGroupId,
        Long partnerId,
        BigDecimal dailyBudget,
        BigDecimal spentAmount,
        LocalDateTime occurredAt
) implements DomainEvent {

    public static AdGroupBudgetExhaustedEvent of(Long adGroupId, Long partnerId,
                                                  BigDecimal dailyBudget, BigDecimal spentAmount) {
        return new AdGroupBudgetExhaustedEvent(adGroupId, partnerId, dailyBudget, spentAmount, LocalDateTime.now());
    }

    @Override
    public String getEventType() {
        return "AD_GROUP_BUDGET_EXHAUSTED";
    }

    @Override
    public String getAggregateType() {
        return "AdGroup";
    }

    @Override
    public Long getAggregateId() {
        return adGroupId;
    }

    @Override
    public String getTopic() {
        return "rtb.adgroup.budget-exhausted";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
