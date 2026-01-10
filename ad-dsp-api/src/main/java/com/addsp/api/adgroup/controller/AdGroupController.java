package com.addsp.api.adgroup.controller;

import com.addsp.api.adgroup.dto.request.CreateAdGroupRequest;
import com.addsp.api.adgroup.dto.request.ToggleStatusRequest;
import com.addsp.api.adgroup.dto.request.UpdateBudgetRequest;
import com.addsp.api.adgroup.dto.request.UpdateNameRequest;
import com.addsp.api.adgroup.dto.request.UpdateScheduleRequest;
import com.addsp.api.adgroup.dto.response.AdGroupResponse;
import com.addsp.api.adgroup.service.AdGroupApplicationService;
import com.addsp.api.security.CustomUserDetails;
import com.addsp.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 광고그룹 API 컨트롤러.
 */
@RestController
@RequestMapping("/api/v1/ad-groups")
@RequiredArgsConstructor
public class AdGroupController {

    private final AdGroupApplicationService adGroupApplicationService;

    /**
     * POST /api/v1/ad-groups
     * 광고그룹 생성.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdGroupResponse>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateAdGroupRequest request) {
        AdGroupResponse response = adGroupApplicationService.create(
                userDetails.getPartnerId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/ad-groups/{id}
     * 광고그룹 단건 조회.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdGroupResponse>> findById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        AdGroupResponse response = adGroupApplicationService.findById(
                userDetails.getPartnerId(), id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/ad-groups
     * 광고그룹 목록 조회.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdGroupResponse>>> findAll(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<AdGroupResponse> response = adGroupApplicationService.findAll(
                userDetails.getPartnerId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * PATCH /api/v1/ad-groups/{id}/budget
     * 광고그룹 예산 수정.
     */
    @PatchMapping("/{id}/budget")
    public ResponseEntity<ApiResponse<AdGroupResponse>> updateBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateBudgetRequest request) {
        AdGroupResponse response = adGroupApplicationService.updateBudget(
                userDetails.getPartnerId(), id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * PATCH /api/v1/ad-groups/{id}/schedule
     * 광고그룹 요일별 스케줄 수정.
     */
    @PatchMapping("/{id}/schedule")
    public ResponseEntity<ApiResponse<AdGroupResponse>> updateSchedule(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateScheduleRequest request) {
        AdGroupResponse response = adGroupApplicationService.updateSchedule(
                userDetails.getPartnerId(), id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * PATCH /api/v1/ad-groups/{id}/name
     * 광고그룹 이름 수정.
     */
    @PatchMapping("/{id}/name")
    public ResponseEntity<ApiResponse<AdGroupResponse>> updateName(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateNameRequest request) {
        AdGroupResponse response = adGroupApplicationService.updateName(
                userDetails.getPartnerId(), id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * PATCH /api/v1/ad-groups/{id}/status
     * 광고그룹 상태 토글 (ON: PENDING/ACTIVE, OFF: STOPPED).
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AdGroupResponse>> toggleStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody ToggleStatusRequest request) {
        AdGroupResponse response = adGroupApplicationService.toggleStatus(
                userDetails.getPartnerId(), id, request.enabled());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * DELETE /api/v1/ad-groups/{id}
     * 광고그룹 삭제 (STOPPED 상태일 때만 가능).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        adGroupApplicationService.delete(userDetails.getPartnerId(), id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
