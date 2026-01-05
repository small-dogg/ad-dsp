package com.addsp.api.auth.service;

import com.addsp.api.auth.dto.request.SignInRequest;
import com.addsp.api.auth.dto.request.SignUpRequest;
import com.addsp.api.auth.dto.request.TokenRefreshRequest;
import com.addsp.api.auth.dto.response.SignUpResponse;
import com.addsp.api.security.jwt.JwtTokenProvider;
import com.addsp.api.security.jwt.TokenResponse;
import com.addsp.common.constant.PartnerStatus;
import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.partner.entity.Partner;
import com.addsp.domain.partner.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private Partner partner;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest(
                "test@example.com",
                "password123",
                "Test Business",
                "123-45-67890",
                "Test Contact",
                "010-1234-5678"
        );

        signInRequest = new SignInRequest(
                "test@example.com",
                "password123"
        );

        partner = Partner.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .businessName("Test Business")
                .businessNumber("123-45-67890")
                .contactName("Test Contact")
                .contactPhone("010-1234-5678")
                .build();
    }

    @Nested
    @DisplayName("SignUp (회원가입)")
    class SignUp {

        @Test
        @DisplayName("유효한 정보로 회원가입을 하면 PENDING 상태의 파트너가 생성된다")
        void signUp_withValidRequest_createsPartnerWithPendingStatus() {
            // given
            given(partnerRepository.existsByEmail(anyString())).willReturn(false);
            given(partnerRepository.existsByBusinessNumber(anyString())).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
            given(partnerRepository.save(any(Partner.class))).willAnswer(invocation -> {
                Partner p = invocation.getArgument(0);
                return Partner.builder()
                        .email(p.getEmail())
                        .password(p.getPassword())
                        .businessName(p.getBusinessName())
                        .businessNumber(p.getBusinessNumber())
                        .contactName(p.getContactName())
                        .contactPhone(p.getContactPhone())
                        .build();
            });

            // when
            SignUpResponse response = authService.signUp(signUpRequest);

            // then
            assertThat(response.email()).isEqualTo(signUpRequest.email());
            assertThat(response.businessName()).isEqualTo(signUpRequest.businessName());
            assertThat(response.status()).isEqualTo(PartnerStatus.PENDING.name());
            verify(passwordEncoder).encode(signUpRequest.password());
            verify(partnerRepository).save(any(Partner.class));
        }

        @Test
        @DisplayName("이미 존재하는 이메일로 회원가입을 하면 예외가 발생한다")
        void signUp_withDuplicateEmail_throwsException() {
            // given
            given(partnerRepository.existsByEmail(anyString())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> authService.signUp(signUpRequest))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.PARTNER_ALREADY_EXISTS);
        }

        @Test
        @DisplayName("이미 존재하는 사업자등록번호로 회원가입을 하면 예외가 발생한다")
        void signUp_withDuplicateBusinessNumber_throwsException() {
            // given
            given(partnerRepository.existsByEmail(anyString())).willReturn(false);
            given(partnerRepository.existsByBusinessNumber(anyString())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> authService.signUp(signUpRequest))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.BUSINESS_NUMBER_ALREADY_EXISTS);
        }
    }

    @Nested
    @DisplayName("SignIn (로그인)")
    class SignIn {

        @Test
        @DisplayName("유효한 자격 증명으로 로그인하면 토큰이 반환된다")
        void signIn_withValidCredentials_returnsTokens() {
            // given
            Partner activePartner = createActivePartner();
            TokenResponse expectedTokens = new TokenResponse("accessToken", "refreshToken", 3600, 604800);

            given(partnerRepository.findByEmail(anyString())).willReturn(Optional.of(activePartner));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
            given(jwtTokenProvider.createTokenPair(any(), anyString())).willReturn(expectedTokens);

            // when
            TokenResponse response = authService.signIn(signInRequest);

            // then
            assertThat(response.accessToken()).isEqualTo("accessToken");
            assertThat(response.refreshToken()).isEqualTo("refreshToken");
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 로그인하면 예외가 발생한다")
        void signIn_withInvalidEmail_throwsException() {
            // given
            given(partnerRepository.findByEmail(anyString())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> authService.signIn(signInRequest))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.INVALID_CREDENTIALS);
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인하면 예외가 발생한다")
        void signIn_withInvalidPassword_throwsException() {
            // given
            given(partnerRepository.findByEmail(anyString())).willReturn(Optional.of(partner));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            // when & then
            assertThatThrownBy(() -> authService.signIn(signInRequest))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.INVALID_CREDENTIALS);
        }

        @Test
        @DisplayName("비활성 상태의 파트너로 로그인하면 예외가 발생한다")
        void signIn_withInactivePartner_throwsException() {
            // given - partner starts with PENDING status
            given(partnerRepository.findByEmail(anyString())).willReturn(Optional.of(partner));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> authService.signIn(signInRequest))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.PARTNER_NOT_ACTIVE);
        }

        private Partner createActivePartner() {
            Partner activePartner = Partner.builder()
                    .email("test@example.com")
                    .password("encodedPassword")
                    .businessName("Test Business")
                    .businessNumber("123-45-67890")
                    .contactName("Test Contact")
                    .contactPhone("010-1234-5678")
                    .build();
            activePartner.activate();
            return activePartner;
        }
    }

    @Nested
    @DisplayName("RefreshToken (토큰 갱신)")
    class RefreshToken {

        @Test
        @DisplayName("유효한 리프레시 토큰으로 새 토큰 쌍을 발급받는다")
        void refreshToken_withValidToken_returnsNewTokenPair() {
            // given
            Partner activePartner = Partner.builder()
                    .email("test@example.com")
                    .password("encodedPassword")
                    .businessName("Test Business")
                    .businessNumber("123-45-67890")
                    .contactName("Test Contact")
                    .contactPhone("010-1234-5678")
                    .build();
            activePartner.activate();

            TokenRefreshRequest request = new TokenRefreshRequest("validRefreshToken");
            TokenResponse expectedTokens = new TokenResponse("newAccessToken", "newRefreshToken", 3600, 604800);

            given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
            given(jwtTokenProvider.getPartnerId(anyString())).willReturn(1L);
            given(jwtTokenProvider.getEmail(anyString())).willReturn("test@example.com");
            given(partnerRepository.findById(any())).willReturn(Optional.of(activePartner));
            given(jwtTokenProvider.createTokenPair(any(), anyString())).willReturn(expectedTokens);

            // when
            TokenResponse response = authService.refreshToken(request);

            // then
            assertThat(response.accessToken()).isEqualTo("newAccessToken");
            assertThat(response.refreshToken()).isEqualTo("newRefreshToken");
        }

        @Test
        @DisplayName("유효하지 않은 리프레시 토큰으로 요청하면 예외가 발생한다")
        void refreshToken_withInvalidToken_throwsException() {
            // given
            TokenRefreshRequest request = new TokenRefreshRequest("invalidRefreshToken");
            given(jwtTokenProvider.validateToken(anyString())).willReturn(false);

            // when & then
            assertThatThrownBy(() -> authService.refreshToken(request))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("존재하지 않는 파트너의 리프레시 토큰으로 요청하면 예외가 발생한다")
        void refreshToken_withNonExistentPartner_throwsException() {
            // given
            TokenRefreshRequest request = new TokenRefreshRequest("validRefreshToken");
            given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
            given(jwtTokenProvider.getPartnerId(anyString())).willReturn(1L);
            given(partnerRepository.findById(any())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> authService.refreshToken(request))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.PARTNER_NOT_FOUND);
        }

        @Test
        @DisplayName("비활성 파트너의 리프레시 토큰으로 요청하면 예외가 발생한다")
        void refreshToken_withInactivePartner_throwsException() {
            // given - partner starts with PENDING status
            TokenRefreshRequest request = new TokenRefreshRequest("validRefreshToken");
            given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
            given(jwtTokenProvider.getPartnerId(anyString())).willReturn(1L);
            given(partnerRepository.findById(any())).willReturn(Optional.of(partner));

            // when & then
            assertThatThrownBy(() -> authService.refreshToken(request))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.PARTNER_NOT_ACTIVE);
        }
    }
}
