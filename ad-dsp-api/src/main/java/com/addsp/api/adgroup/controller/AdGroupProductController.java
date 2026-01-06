package com.addsp.api.adgroup.controller;

import com.addsp.api.adgroup.dto.request.AddProductsRequest;
import com.addsp.api.adgroup.dto.response.AdGroupProductResponse;
import com.addsp.api.adgroup.dto.response.ProductPageResponse;
import com.addsp.api.adgroup.dto.response.ProductResponse;
import com.addsp.api.adgroup.service.AdGroupProductApplicationService;
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
 * 광고그룹-상품 API 컨트롤러.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdGroupProductController {

    private final AdGroupProductApplicationService adGroupProductApplicationService;

    /**
     * POST /api/v1/ad-groups/{adGroupId}/products
     * 광고그룹에 상품 추가.
     */
    @PostMapping("/ad-groups/{adGroupId}/products")
    public ResponseEntity<ApiResponse<List<AdGroupProductResponse>>> addProducts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long adGroupId,
            @Valid @RequestBody AddProductsRequest request) {
        List<AdGroupProductResponse> response = adGroupProductApplicationService.addProducts(
                userDetails.getPartnerId(), adGroupId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/ad-groups/{adGroupId}/products
     * 광고그룹의 상품 목록 조회.
     */
    @GetMapping("/ad-groups/{adGroupId}/products")
    public ResponseEntity<ApiResponse<List<AdGroupProductResponse>>> findByAdGroupId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long adGroupId) {
        List<AdGroupProductResponse> response = adGroupProductApplicationService.findByAdGroupId(
                userDetails.getPartnerId(), adGroupId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * DELETE /api/v1/ad-groups/{adGroupId}/products/{productId}
     * 광고그룹에서 상품 삭제.
     */
    @DeleteMapping("/ad-groups/{adGroupId}/products/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeProduct(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long adGroupId,
            @PathVariable Long productId) {
        adGroupProductApplicationService.removeProduct(
                userDetails.getPartnerId(), adGroupId, productId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * GET /api/v1/products
     * 외부 상품 API 목록 조회 (프록시).
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<ProductPageResponse>> getProducts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String searchType,
            @RequestParam(defaultValue = "ID") String sortType) {
        ProductPageResponse response = adGroupProductApplicationService.getProducts(
                userDetails.getPartnerId(), page, limit, searchKeyword, searchType, sortType);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/products/{productId}
     * 외부 상품 API 단건 조회 (프록시).
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            @PathVariable Long productId) {
        ProductResponse response = adGroupProductApplicationService.getProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
