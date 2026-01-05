package com.addsp.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CampaignStatus {

    READY("대기"),
    ACTIVE("진행중"),
    PAUSED("일시정지"),
    ENDED("종료");

    private final String description;
}
