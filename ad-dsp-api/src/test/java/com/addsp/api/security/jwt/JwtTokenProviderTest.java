package com.addsp.api.security.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private JwtProperties jwtProperties;

    private static final Long PARTNER_ID = 1L;
    private static final String EMAIL = "test@example.com";
    // Base64 encoded secret key (at least 256 bits for HS256)
    private static final String SECRET = "dGhpc0lzQVZlcnlMb25nU2VjcmV0S2V5Rm9yVGVzdGluZ1B1cnBvc2VzT25seURvTm90VXNlSW5Qcm9kdWN0aW9u";
    private static final long ACCESS_TOKEN_EXPIRATION = 3600000L; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000L; // 7 days

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties(SECRET, ACCESS_TOKEN_EXPIRATION, REFRESH_TOKEN_EXPIRATION);
        jwtTokenProvider = new JwtTokenProvider(jwtProperties);
    }

    @Nested
    @DisplayName("Access Token 생성")
    class CreateAccessToken {

        @Test
        @DisplayName("유효한 정보로 Access Token을 생성한다")
        void createAccessToken_withValidInfo_returnsToken() {
            // when
            String token = jwtTokenProvider.createAccessToken(PARTNER_ID, EMAIL);

            // then
            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
        }

        @Test
        @DisplayName("Access Token에서 Partner ID를 추출할 수 있다")
        void createAccessToken_extractPartnerId_returnsCorrectId() {
            // when
            String token = jwtTokenProvider.createAccessToken(PARTNER_ID, EMAIL);

            // then
            Long extractedId = jwtTokenProvider.getPartnerId(token);
            assertThat(extractedId).isEqualTo(PARTNER_ID);
        }

        @Test
        @DisplayName("Access Token에서 Email을 추출할 수 있다")
        void createAccessToken_extractEmail_returnsCorrectEmail() {
            // when
            String token = jwtTokenProvider.createAccessToken(PARTNER_ID, EMAIL);

            // then
            String extractedEmail = jwtTokenProvider.getEmail(token);
            assertThat(extractedEmail).isEqualTo(EMAIL);
        }
    }

    @Nested
    @DisplayName("Refresh Token 생성")
    class CreateRefreshToken {

        @Test
        @DisplayName("유효한 정보로 Refresh Token을 생성한다")
        void createRefreshToken_withValidInfo_returnsToken() {
            // when
            String token = jwtTokenProvider.createRefreshToken(PARTNER_ID, EMAIL);

            // then
            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
        }

        @Test
        @DisplayName("Refresh Token에서 Partner ID를 추출할 수 있다")
        void createRefreshToken_extractPartnerId_returnsCorrectId() {
            // when
            String token = jwtTokenProvider.createRefreshToken(PARTNER_ID, EMAIL);

            // then
            Long extractedId = jwtTokenProvider.getPartnerId(token);
            assertThat(extractedId).isEqualTo(PARTNER_ID);
        }
    }

    @Nested
    @DisplayName("Token Pair 생성")
    class CreateTokenPair {

        @Test
        @DisplayName("Access Token과 Refresh Token을 함께 생성한다")
        void createTokenPair_returnsTokenResponse() {
            // when
            TokenResponse response = jwtTokenProvider.createTokenPair(PARTNER_ID, EMAIL);

            // then
            assertThat(response).isNotNull();
            assertThat(response.accessToken()).isNotNull();
            assertThat(response.refreshToken()).isNotNull();
            assertThat(response.accessTokenExpiresIn()).isGreaterThan(0);
            assertThat(response.refreshTokenExpiresIn()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Token 검증")
    class ValidateToken {

        @Test
        @DisplayName("유효한 토큰은 true를 반환한다")
        void validateToken_withValidToken_returnsTrue() {
            // given
            String token = jwtTokenProvider.createAccessToken(PARTNER_ID, EMAIL);

            // when
            boolean isValid = jwtTokenProvider.validateToken(token);

            // then
            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("잘못된 서명의 토큰은 false를 반환한다")
        void validateToken_withInvalidSignature_returnsFalse() {
            // given
            String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

            // when
            boolean isValid = jwtTokenProvider.validateToken(invalidToken);

            // then
            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("null 토큰은 false를 반환한다")
        void validateToken_withNullToken_returnsFalse() {
            // when
            boolean isValid = jwtTokenProvider.validateToken(null);

            // then
            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("빈 토큰은 false를 반환한다")
        void validateToken_withEmptyToken_returnsFalse() {
            // when
            boolean isValid = jwtTokenProvider.validateToken("");

            // then
            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("만료된 토큰은 false를 반환한다")
        void validateToken_withExpiredToken_returnsFalse() {
            // given - create provider with very short expiration
            JwtProperties shortExpProps = new JwtProperties(SECRET, 1L, 1L); // 1ms expiration
            JwtTokenProvider shortExpProvider = new JwtTokenProvider(shortExpProps);
            String token = shortExpProvider.createAccessToken(PARTNER_ID, EMAIL);

            // wait for token to expire
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // when
            boolean isValid = shortExpProvider.validateToken(token);

            // then
            assertThat(isValid).isFalse();
        }
    }

    @Nested
    @DisplayName("Claims 추출")
    class GetClaims {

        @Test
        @DisplayName("토큰에서 Claims를 추출할 수 있다")
        void getClaims_withValidToken_returnsClaims() {
            // given
            String token = jwtTokenProvider.createAccessToken(PARTNER_ID, EMAIL);

            // when
            Claims claims = jwtTokenProvider.getClaims(token);

            // then
            assertThat(claims).isNotNull();
            assertThat(claims.getSubject()).isEqualTo(EMAIL);
            assertThat(claims.get("partnerId", Long.class)).isEqualTo(PARTNER_ID);
        }
    }
}
