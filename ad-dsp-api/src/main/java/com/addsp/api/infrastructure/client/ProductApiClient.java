package com.addsp.api.infrastructure.client;

import com.addsp.api.infrastructure.client.dto.ProductApiResponse;
import com.addsp.api.infrastructure.client.dto.ProductPageApiResponse;
import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Client for external Product API.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductApiClient {

    private final RestTemplate restTemplate;

    @Value("${product-api.base-url}")
    private String baseUrl;

    /**
     * Get paginated product list.
     */
    public ProductPageApiResponse getProducts(int page, int limit, Long partnerId,
                                               String searchKeyword, String searchType, String sortType) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                    .path("/products")
                    .queryParam("page", page)
                    .queryParam("limit", limit)
                    .queryParam("partnerId", partnerId)
                    .queryParam("sortType", sortType != null ? sortType : "ID");

            if (searchKeyword != null && !searchKeyword.isBlank()) {
                builder.queryParam("searchKeyword", searchKeyword);
                builder.queryParam("searchType", searchType != null ? searchType : "NAME");
            }

            String url = builder.toUriString();
            log.debug("Calling Product API: {}", url);

            return restTemplate.getForObject(url, ProductPageApiResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Products not found for partnerId: {}", partnerId);
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        } catch (RestClientException e) {
            log.error("Product API call failed", e);
            throw new BusinessException(ErrorCode.PRODUCT_API_ERROR);
        }
    }

    /**
     * Get single product by ID.
     */
    public ProductApiResponse getProduct(Long productId) {
        try {
            String url = UriComponentsBuilder.fromUriString(baseUrl)
                    .path("/products/{productId}")
                    .buildAndExpand(productId)
                    .toUriString();

            log.debug("Calling Product API: {}", url);

            return restTemplate.getForObject(url, ProductApiResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Product not found: {}", productId);
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        } catch (RestClientException e) {
            log.error("Product API call failed for productId: {}", productId, e);
            throw new BusinessException(ErrorCode.PRODUCT_API_ERROR);
        }
    }
}
