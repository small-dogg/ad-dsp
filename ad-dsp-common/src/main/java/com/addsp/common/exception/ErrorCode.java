package com.addsp.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    ENTITY_NOT_FOUND(404, "C002", "엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 오류가 발생했습니다."),

    // Partner
    PARTNER_NOT_FOUND(404, "P001", "파트너를 찾을 수 없습니다."),
    PARTNER_ALREADY_EXISTS(409, "P002", "이미 존재하는 파트너입니다."),
    PARTNER_NOT_ACTIVE(403, "P003", "비활성 상태의 파트너입니다."),

    // Campaign
    CAMPAIGN_NOT_FOUND(404, "CA001", "캠페인을 찾을 수 없습니다."),
    CAMPAIGN_BUDGET_EXCEEDED(400, "CA002", "캠페인 예산이 초과되었습니다."),
    CAMPAIGN_NOT_ACTIVE(403, "CA003", "비활성 상태의 캠페인입니다."),

    // AdGroup
    AD_GROUP_NOT_FOUND(404, "AG001", "광고그룹을 찾을 수 없습니다."),
    AD_GROUP_NAME_DUPLICATE(409, "AG002", "동일한 이름의 광고그룹이 이미 존재합니다."),
    AD_GROUP_CANNOT_DELETE(400, "AG003", "활성 상태의 광고그룹은 삭제할 수 없습니다."),
    AD_GROUP_INVALID_BUDGET(400, "AG004", "일 예산은 최소 10,000원 이상이어야 합니다."),

    // Ad
    AD_NOT_FOUND(404, "AD001", "광고를 찾을 수 없습니다."),

    // Keyword
    KEYWORD_NOT_FOUND(404, "K001", "키워드를 찾을 수 없습니다."),
    KEYWORD_ALREADY_EXISTS(409, "K002", "이미 등록된 키워드입니다."),

    // Billing
    INSUFFICIENT_BALANCE(400, "B001", "잔액이 부족합니다."),
    INVALID_CHARGE_AMOUNT(400, "B002", "유효하지 않은 충전 금액입니다."),

    // Authentication
    INVALID_CREDENTIALS(401, "A001", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(401, "A002", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "A003", "만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN(401, "A004", "유효하지 않은 리프레시 토큰입니다."),
    BUSINESS_NUMBER_ALREADY_EXISTS(409, "P004", "이미 등록된 사업자등록번호입니다."),

    // Product
    PRODUCT_NOT_FOUND(404, "PR001", "상품을 찾을 수 없습니다."),
    PRODUCT_ALREADY_EXISTS(409, "PR002", "이미 등록된 상품입니다."),
    PRODUCT_API_ERROR(502, "PR003", "상품 API 호출에 실패했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
