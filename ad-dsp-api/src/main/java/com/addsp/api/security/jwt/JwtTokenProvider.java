package com.addsp.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
 * JWT token creation and validation utility.
 * Uses HS256 algorithm with secret key from properties.
 */
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.secret());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Creates access token with partner ID and email.
     */
    public String createAccessToken(Long partnerId, String email) {
        return createToken(partnerId, email, jwtProperties.accessTokenExpiration());
    }

    /**
     * Creates refresh token with partner ID and email.
     */
    public String createRefreshToken(Long partnerId, String email) {
        return createToken(partnerId, email, jwtProperties.refreshTokenExpiration());
    }

    /**
     * Creates both access and refresh tokens.
     */
    public TokenResponse createTokenPair(Long partnerId, String email) {
        String accessToken = createAccessToken(partnerId, email);
        String refreshToken = createRefreshToken(partnerId, email);

        return new TokenResponse(
                accessToken,
                refreshToken,
                jwtProperties.accessTokenExpiration() / 1000,
                jwtProperties.refreshTokenExpiration() / 1000
        );
    }

    /**
     * Validates token signature and expiration.
     */
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extracts claims from token.
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts partner ID from token.
     */
    public Long getPartnerId(String token) {
        return getClaims(token).get("partnerId", Long.class);
    }

    /**
     * Extracts email (subject) from token.
     */
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    private String createToken(Long partnerId, String email, long expirationMs) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("partnerId", partnerId)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }
}
