package com.addsp.api.adgroup.dto.request;

import com.addsp.common.constant.BudgetType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * 광고그룹 예산 수정 요청.
 */
public record UpdateBudgetRequest(
        @NotNull(message = "예산 유형은 필수입니다.")
        BudgetType budgetType,

        BigDecimal dailyBudget
) {
}
