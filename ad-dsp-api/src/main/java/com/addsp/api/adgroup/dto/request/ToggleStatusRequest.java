package com.addsp.api.adgroup.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * 광고그룹 상태 토글 요청.
 */
public record ToggleStatusRequest(
        @NotNull(message = "enabled 값은 필수입니다.")
        Boolean enabled
) {
}
