package com.addsp.api.adgroup.service;

import com.addsp.api.adgroup.dto.request.AddProductsRequest;
import com.addsp.api.adgroup.dto.response.AdGroupProductResponse;
import com.addsp.api.adgroup.dto.response.ProductPageResponse;
import com.addsp.api.adgroup.dto.response.ProductResponse;
import com.addsp.api.infrastructure.client.ProductApiClient;
import com.addsp.api.infrastructure.client.dto.ProductApiResponse;
import com.addsp.api.infrastructure.client.dto.ProductPageApiResponse;
import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.adgroup.entity.AdGroup;
import com.addsp.domain.adgroup.service.AdGroupService;
import com.addsp.domain.keyword.service.AdKeywordService;
import com.addsp.domain.product.entity.AdGroupProduct;
import com.addsp.domain.product.repository.AdGroupProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdGroupProductApplicationServiceTest {

    @InjectMocks
    private AdGroupProductApplicationService adGroupProductApplicationService;

    @Mock
    private AdGroupProductRepository adGroupProductRepository;

    @Mock
    private AdGroupService adGroupService;

    @Mock
    private ProductApiClient productApiClient;

    @Mock
    private AdKeywordService adKeywordService;

    private static final Long PARTNER_ID = 1L;
    private static final Long AD_GROUP_ID = 100L;
    private static final Long PRODUCT_ID = 200L;

    private AdGroup adGroup;
    private ProductApiResponse productApiResponse;

    @BeforeEach
    void setUp() {
        adGroup = AdGroup.builder()
                .partnerId(PARTNER_ID)
                .name("Test AdGroup")
                .build();

        productApiResponse = new ProductApiResponse(
                PRODUCT_ID,
                "Test Product",
                10000L,
                "http://example.com/image.jpg",
                PARTNER_ID,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Nested
    @DisplayName("AddProducts (상품 추가)")
    class AddProducts {

        @Test
        @DisplayName("유효한 상품을 광고그룹에 추가하면 성공한다")
        void addProducts_withValidRequest_succeeds() {
            // given
            AddProductsRequest request = new AddProductsRequest(List.of(PRODUCT_ID));

            given(adGroupService.findByIdAndPartnerId(AD_GROUP_ID, PARTNER_ID)).willReturn(adGroup);
            given(adGroupProductRepository.existsByAdGroupIdAndProductId(AD_GROUP_ID, PRODUCT_ID)).willReturn(false);
            given(productApiClient.getProduct(PRODUCT_ID)).willReturn(productApiResponse);
            given(adGroupProductRepository.save(any(AdGroupProduct.class))).willAnswer(invocation -> {
                AdGroupProduct agp = invocation.getArgument(0);
                return AdGroupProduct.builder()
                        .adGroupId(agp.getAdGroupId())
                        .productId(agp.getProductId())
                        .partnerId(agp.getPartnerId())
                        .build();
            });

            // when
            List<AdGroupProductResponse> responses = adGroupProductApplicationService.addProducts(
                    PARTNER_ID, AD_GROUP_ID, request);

            // then
            assertThat(responses).hasSize(1);
            assertThat(responses.get(0).adGroupId()).isEqualTo(AD_GROUP_ID);
            assertThat(responses.get(0).productId()).isEqualTo(PRODUCT_ID);
            verify(adGroupProductRepository).save(any(AdGroupProduct.class));
        }

        @Test
        @DisplayName("이미 등록된 상품을 추가하면 예외가 발생한다")
        void addProducts_withDuplicateProduct_throwsException() {
            // given
            AddProductsRequest request = new AddProductsRequest(List.of(PRODUCT_ID));

            given(adGroupService.findByIdAndPartnerId(AD_GROUP_ID, PARTNER_ID)).willReturn(adGroup);
            given(adGroupProductRepository.existsByAdGroupIdAndProductId(AD_GROUP_ID, PRODUCT_ID)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> adGroupProductApplicationService.addProducts(PARTNER_ID, AD_GROUP_ID, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        @Test
        @DisplayName("존재하지 않는 광고그룹에 상품을 추가하면 예외가 발생한다")
        void addProducts_withInvalidAdGroup_throwsException() {
            // given
            AddProductsRequest request = new AddProductsRequest(List.of(PRODUCT_ID));

            given(adGroupService.findByIdAndPartnerId(AD_GROUP_ID, PARTNER_ID))
                    .willThrow(new BusinessException(ErrorCode.AD_GROUP_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> adGroupProductApplicationService.addProducts(PARTNER_ID, AD_GROUP_ID, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.AD_GROUP_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("FindByAdGroupId (상품 목록 조회)")
    class FindByAdGroupId {

        @Test
        @DisplayName("광고그룹의 상품 목록을 조회하면 성공한다")
        void findByAdGroupId_returnsProductList() {
            // given
            AdGroupProduct adGroupProduct = AdGroupProduct.builder()
                    .adGroupId(AD_GROUP_ID)
                    .productId(PRODUCT_ID)
                    .partnerId(PARTNER_ID)
                    .build();

            given(adGroupService.findByIdAndPartnerId(AD_GROUP_ID, PARTNER_ID)).willReturn(adGroup);
            given(adGroupProductRepository.findByAdGroupIdAndPartnerId(AD_GROUP_ID, PARTNER_ID))
                    .willReturn(List.of(adGroupProduct));

            // when
            List<AdGroupProductResponse> responses = adGroupProductApplicationService.findByAdGroupId(
                    PARTNER_ID, AD_GROUP_ID);

            // then
            assertThat(responses).hasSize(1);
            assertThat(responses.get(0).productId()).isEqualTo(PRODUCT_ID);
        }
    }

    @Nested
    @DisplayName("RemoveProduct (상품 삭제)")
    class RemoveProduct {

        @Test
        @DisplayName("광고그룹에서 상품을 삭제하면 연관된 광고 키워드도 함께 삭제된다")
        void removeProduct_succeeds() {
            // given
            AdGroupProduct adGroupProduct = AdGroupProduct.builder()
                    .adGroupId(AD_GROUP_ID)
                    .productId(PRODUCT_ID)
                    .partnerId(PARTNER_ID)
                    .build();

            given(adGroupService.findByIdAndPartnerId(AD_GROUP_ID, PARTNER_ID)).willReturn(adGroup);
            given(adGroupProductRepository.findByAdGroupIdAndProductId(AD_GROUP_ID, PRODUCT_ID))
                    .willReturn(Optional.of(adGroupProduct));

            // when
            adGroupProductApplicationService.removeProduct(PARTNER_ID, AD_GROUP_ID, PRODUCT_ID);

            // then
            verify(adKeywordService).deleteAllByAdGroupIdAndDealId(AD_GROUP_ID, PRODUCT_ID);
            verify(adGroupProductRepository).deleteByAdGroupIdAndProductId(AD_GROUP_ID, PRODUCT_ID);
        }

        @Test
        @DisplayName("존재하지 않는 상품을 삭제하면 예외가 발생한다")
        void removeProduct_withNonExistentProduct_throwsException() {
            // given
            given(adGroupService.findByIdAndPartnerId(AD_GROUP_ID, PARTNER_ID)).willReturn(adGroup);
            given(adGroupProductRepository.findByAdGroupIdAndProductId(AD_GROUP_ID, PRODUCT_ID))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> adGroupProductApplicationService.removeProduct(
                    PARTNER_ID, AD_GROUP_ID, PRODUCT_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
        }

        @Test
        @DisplayName("다른 파트너의 상품을 삭제하면 예외가 발생한다")
        void removeProduct_withDifferentPartner_throwsException() {
            // given
            Long differentPartnerId = 999L;
            AdGroupProduct adGroupProduct = AdGroupProduct.builder()
                    .adGroupId(AD_GROUP_ID)
                    .productId(PRODUCT_ID)
                    .partnerId(differentPartnerId)
                    .build();

            given(adGroupService.findByIdAndPartnerId(AD_GROUP_ID, PARTNER_ID)).willReturn(adGroup);
            given(adGroupProductRepository.findByAdGroupIdAndProductId(AD_GROUP_ID, PRODUCT_ID))
                    .willReturn(Optional.of(adGroupProduct));

            // when & then
            assertThatThrownBy(() -> adGroupProductApplicationService.removeProduct(
                    PARTNER_ID, AD_GROUP_ID, PRODUCT_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(ex -> ((BusinessException) ex).getErrorCode())
                    .isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("GetProducts (외부 상품 API 프록시)")
    class GetProducts {

        @Test
        @DisplayName("외부 상품 API 목록 조회가 성공한다")
        void getProducts_returnsPagedProducts() {
            // given
            ProductPageApiResponse.ProductItem productItem = new ProductPageApiResponse.ProductItem(
                    PRODUCT_ID,
                    PARTNER_ID,
                    "Test Product",
                    "ACTIVE",
                    10000L,
                    "http://example.com/image.jpg",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            ProductPageApiResponse.PageMeta pageMeta = new ProductPageApiResponse.PageMeta(
                    0, 20, 1, 1, false
            );
            ProductPageApiResponse apiResponse = new ProductPageApiResponse(
                    List.of(productItem),
                    pageMeta
            );

            given(productApiClient.getProducts(0, 20, PARTNER_ID, null, null, "ID"))
                    .willReturn(apiResponse);

            // when
            ProductPageResponse response = adGroupProductApplicationService.getProducts(
                    PARTNER_ID, 0, 20, null, null, "ID");

            // then
            assertThat(response.content()).hasSize(1);
            assertThat(response.content().get(0).id()).isEqualTo(PRODUCT_ID);
            assertThat(response.totalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("외부 상품 API 단건 조회가 성공한다")
        void getProduct_returnsProduct() {
            // given
            given(productApiClient.getProduct(PRODUCT_ID)).willReturn(productApiResponse);

            // when
            ProductResponse response = adGroupProductApplicationService.getProduct(PRODUCT_ID);

            // then
            assertThat(response.id()).isEqualTo(PRODUCT_ID);
            assertThat(response.name()).isEqualTo("Test Product");
        }
    }
}
