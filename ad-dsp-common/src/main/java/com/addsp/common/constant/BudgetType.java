package com.addsp.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BudgetType {
    LIMITED("제한"),
    UNLIMITED("무제한");

    private final String description;
}
