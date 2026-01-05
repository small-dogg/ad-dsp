package com.addsp.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BillingType {

    CPC("클릭당 과금"),
    CPM("노출당 과금");

    private final String description;
}
