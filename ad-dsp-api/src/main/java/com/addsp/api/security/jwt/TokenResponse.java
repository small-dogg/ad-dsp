package com.addsp.api.security.jwt;

/**
 * JWT token pair response containing access and refresh tokens.
 */
public record TokenResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresIn,
        long refreshTokenExpiresIn
) {
}
