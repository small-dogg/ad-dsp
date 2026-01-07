package com.addsp.api.adgroup.controller;

import com.addsp.api.adgroup.dto.request.CreateAdKeywordRequest;
import com.addsp.api.adgroup.dto.request.UpdateAdKeywordBidRequest;
import com.addsp.api.adgroup.dto.response.AdKeywordResponse;
import com.addsp.api.adgroup.service.AdKeywordApplicationService;
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
 * 광고 키워드 API 컨트롤러.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdKeywordController {

    private final AdKeywordApplicationService adKeywordApplicationService;

    /**
     * POST /api/v1/ad-groups/{adGroupId}/keywords
     * 광고그룹에 광고 키워드 추가.
     */
    @PostMapping("/ad-groups/{adGroupId}/keywords")
    public ResponseEntity<ApiResponse<AdKeywordResponse>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long adGroupId,
            @Valid @RequestBody CreateAdKeywordRequest request) {
        AdKeywordResponse response = adKeywordApplicationService.create(
                userDetails.getPartnerId(), adGroupId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/ad-groups/{adGroupId}/keywords
     * 광고그룹의 광고 키워드 목록 조회.
     */
    @GetMapping("/ad-groups/{adGroupId}/keywords")
    public ResponseEntity<ApiResponse<List<AdKeywordResponse>>> findByAdGroupId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long adGroupId) {
        List<AdKeywordResponse> response = adKeywordApplicationService.findByAdGroupId(
                userDetails.getPartnerId(), adGroupId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/ad-groups/{adGroupId}/keywords/deal/{dealId}
     * 광고그룹의 특정 상품에 대한 광고 키워드 목록 조회.
     */
    @GetMapping("/ad-groups/{adGroupId}/keywords/deal/{dealId}")
    public ResponseEntity<ApiResponse<List<AdKeywordResponse>>> findByAdGroupIdAndDealId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long adGroupId,
            @PathVariable Long dealId) {
        List<AdKeywordResponse> response = adKeywordApplicationService.findByAdGroupIdAndDealId(
                userDetails.getPartnerId(), adGroupId, dealId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * PATCH /api/v1/ad-groups/{adGroupId}/keywords/{keywordId}/bid
     * 광고 키워드 입찰가 수정.
     */
    @PatchMapping("/ad-groups/{adGroupId}/keywords/{keywordId}/bid")
    public ResponseEntity<ApiResponse<AdKeywordResponse>> updateBidAmount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long adGroupId,
            @PathVariable Long keywordId,
            @Valid @RequestBody UpdateAdKeywordBidRequest request) {
        AdKeywordResponse response = adKeywordApplicationService.updateBidAmount(
                userDetails.getPartnerId(), adGroupId, keywordId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * DELETE /api/v1/ad-groups/{adGroupId}/keywords/{keywordId}
     * 광고 키워드 삭제.
     */
    @DeleteMapping("/ad-groups/{adGroupId}/keywords/{keywordId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long adGroupId,
            @PathVariable Long keywordId) {
        adKeywordApplicationService.delete(
                userDetails.getPartnerId(), adGroupId, keywordId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
