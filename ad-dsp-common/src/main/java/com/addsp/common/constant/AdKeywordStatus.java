package com.addsp.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 광고 키워드 상태.
 */
@Getter
@RequiredArgsConstructor
public enum AdKeywordStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절"),
    INACTIVE("비활성");

    private final String description;
}
