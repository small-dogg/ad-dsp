package com.addsp.api.auth.controller;

import com.addsp.api.auth.dto.request.SignInRequest;
import com.addsp.api.auth.dto.request.SignUpRequest;
import com.addsp.api.auth.dto.request.TokenRefreshRequest;
import com.addsp.api.auth.dto.response.SignUpResponse;
import com.addsp.api.auth.service.AuthService;
import com.addsp.api.security.jwt.TokenResponse;
import com.addsp.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoints for partner registration and login.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/v1/auth/signup
     * Partner registration endpoint.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
            @Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * POST /api/v1/auth/signin
     * Partner authentication endpoint.
     */
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<TokenResponse>> signIn(
            @Valid @RequestBody SignInRequest request) {
        TokenResponse response = authService.signIn(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * POST /api/v1/auth/refresh
     * Token refresh endpoint.
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request) {
        TokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
