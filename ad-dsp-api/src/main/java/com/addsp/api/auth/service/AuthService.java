package com.addsp.api.auth.service;

import com.addsp.api.auth.dto.request.SignInRequest;
import com.addsp.api.auth.dto.request.SignUpRequest;
import com.addsp.api.auth.dto.request.TokenRefreshRequest;
import com.addsp.api.auth.dto.response.SignUpResponse;
import com.addsp.api.security.jwt.JwtTokenProvider;
import com.addsp.api.security.jwt.TokenResponse;
import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.partner.entity.Partner;
import com.addsp.domain.partner.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication service for partner registration and login.
 * Handles signup, signin, and token refresh operations.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registers a new partner.
     * Validates email and business number uniqueness.
     * Creates partner with PENDING status.
     */
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (partnerRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.PARTNER_ALREADY_EXISTS);
        }

        if (partnerRepository.existsByBusinessNumber(request.businessNumber())) {
            throw new BusinessException(ErrorCode.BUSINESS_NUMBER_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Partner partner = Partner.builder()
                .email(request.email())
                .password(encodedPassword)
                .businessName(request.businessName())
                .businessNumber(request.businessNumber())
                .contactName(request.contactName())
                .contactPhone(request.contactPhone())
                .build();

        Partner saved = partnerRepository.save(partner);

        return new SignUpResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getBusinessName(),
                saved.getStatus().name()
        );
    }

    /**
     * Authenticates partner and issues JWT tokens.
     * Validates credentials and partner status (must be ACTIVE).
     */
    public TokenResponse signIn(SignInRequest request) {
        Partner partner = partnerRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), partner.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (!partner.isActive()) {
            throw new BusinessException(ErrorCode.PARTNER_NOT_ACTIVE);
        }

        return jwtTokenProvider.createTokenPair(partner.getId(), partner.getEmail());
    }

    /**
     * Refreshes access token using refresh token.
     * Validates refresh token and partner status.
     */
    public TokenResponse refreshToken(TokenRefreshRequest request) {
        if (!jwtTokenProvider.validateToken(request.refreshToken())) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long partnerId = jwtTokenProvider.getPartnerId(request.refreshToken());
        String email = jwtTokenProvider.getEmail(request.refreshToken());

        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTNER_NOT_FOUND));

        if (!partner.isActive()) {
            throw new BusinessException(ErrorCode.PARTNER_NOT_ACTIVE);
        }

        return jwtTokenProvider.createTokenPair(partnerId, email);
    }
}
