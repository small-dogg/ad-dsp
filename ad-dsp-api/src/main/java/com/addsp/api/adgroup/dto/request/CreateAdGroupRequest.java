package com.addsp.api.adgroup.dto.request;

import com.addsp.common.constant.BudgetType;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * 광고그룹 생성 요청.
 */
public record CreateAdGroupRequest(
        @NotBlank(message = "광고그룹명은 필수입니다.")
        String name,

        BudgetType budgetType,

        BigDecimal dailyBudget,

        Boolean mon,
        Boolean tue,
        Boolean wed,
        Boolean thu,
        Boolean fri,
        Boolean sat,
        Boolean sun
) {
}
