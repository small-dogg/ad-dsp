package com.addsp.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdStatus {

    ACTIVE("활성"),
    PAUSED("일시정지"),
    ENDED("종료");

    private final String description;
}
