package com.addsp.api.adgroup.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 광고그룹 이름 수정 요청.
 */
public record UpdateNameRequest(
        @NotBlank(message = "광고그룹명은 필수입니다.")
        String name
) {
}
