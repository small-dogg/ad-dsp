package com.addsp.api.auth.dto.response;

/**
 * Partner registration response.
 */
public record SignUpResponse(
        Long partnerId,
        String email,
        String businessName,
        String status
) {
}
