package com.addsp.api.billing.controller;

import com.addsp.api.billing.dto.request.ChargeCashRequest;
import com.addsp.api.billing.dto.request.DeductCashRequest;
import com.addsp.api.billing.dto.response.CashBalanceResponse;
import com.addsp.api.billing.dto.response.CashResponse;
import com.addsp.api.billing.service.CashApplicationService;
import com.addsp.api.security.CustomUserDetails;
import com.addsp.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 캐시 관리 API 컨트롤러.
 */
@RestController
@RequestMapping("/api/v1/cash")
@RequiredArgsConstructor
public class CashController {

    private final CashApplicationService cashApplicationService;

    /**
     * 캐시 충전.
     */
    @PostMapping("/charge")
    public ResponseEntity<ApiResponse<CashResponse>> charge(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChargeCashRequest request
    ) {
        CashResponse response = cashApplicationService.charge(userDetails.getPartnerId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 캐시 차감.
     */
    @PostMapping("/deduct")
    public ResponseEntity<ApiResponse<Void>> deduct(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DeductCashRequest request
    ) {
        cashApplicationService.deduct(userDetails.getPartnerId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 잔액 조회.
     */
    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<CashBalanceResponse>> getBalance(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CashBalanceResponse response = cashApplicationService.getBalance(userDetails.getPartnerId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 캐시 목록 조회.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CashResponse>>> findAll(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<CashResponse> responses = cashApplicationService.findByPartnerId(userDetails.getPartnerId());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
