package com.addsp.api.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Token refresh request.
 */
public record TokenRefreshRequest(
        @NotBlank(message = "리프레시 토큰은 필수입니다.")
        String refreshToken
) {
}
