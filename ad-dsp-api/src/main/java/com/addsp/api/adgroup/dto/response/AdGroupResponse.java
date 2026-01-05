package com.addsp.api.adgroup.dto.response;

import com.addsp.domain.adgroup.entity.AdGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 광고그룹 응답.
 */
public record AdGroupResponse(
        Long id,
        Long partnerId,
        String name,
        String budgetType,
        BigDecimal dailyBudget,
        BigDecimal spentAmount,
        Boolean mon,
        Boolean tue,
        Boolean wed,
        Boolean thu,
        Boolean fri,
        Boolean sat,
        Boolean sun,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AdGroupResponse from(AdGroup adGroup) {
        return new AdGroupResponse(
                adGroup.getId(),
                adGroup.getPartnerId(),
                adGroup.getName(),
                adGroup.getBudgetType().name(),
                adGroup.getDailyBudget(),
                adGroup.getSpentAmount(),
                adGroup.getMon(),
                adGroup.getTue(),
                adGroup.getWed(),
                adGroup.getThu(),
                adGroup.getFri(),
                adGroup.getSat(),
                adGroup.getSun(),
                adGroup.getStatus().name(),
                adGroup.getCreatedAt(),
                adGroup.getUpdatedAt()
        );
    }
}
