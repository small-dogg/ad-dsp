package com.addsp.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KeywordMatchType {

    EXACT("정확"),
    PHRASE("구문"),
    BROAD("확장");

    private final String description;
}
