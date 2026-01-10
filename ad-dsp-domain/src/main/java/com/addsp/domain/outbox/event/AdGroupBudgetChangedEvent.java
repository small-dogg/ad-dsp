package com.addsp.domain.outbox.event;

import com.addsp.common.constant.BudgetType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when AdGroup budget or budgetType changes.
 */
public record AdGroupBudgetChangedEvent(
        Long adGroupId,
        Long partnerId,
        BudgetType budgetType,
        BigDecimal dailyBudget,
        LocalDateTime occurredAt
) implements DomainEvent {

    public static AdGroupBudgetChangedEvent of(Long adGroupId, Long partnerId,
                                                BudgetType budgetType, BigDecimal dailyBudget) {
        return new AdGroupBudgetChangedEvent(adGroupId, partnerId, budgetType, dailyBudget, LocalDateTime.now());
    }

    @Override
    public String getEventType() {
        return "AD_GROUP_BUDGET_CHANGED";
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
        return "rtb.adgroup.budget-changed";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
