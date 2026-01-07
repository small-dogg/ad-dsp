package com.addsp.api.adgroup.dto.response;

import com.addsp.domain.keyword.entity.AdKeyword;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 광고 키워드 응답.
 */
public record AdKeywordResponse(
        Long id,
        Long adGroupId,
        Long dealId,
        Long keywordId,
        String keyword,
        String status,
        BigDecimal bidAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AdKeywordResponse from(AdKeyword adKeyword, String keywordText) {
        return new AdKeywordResponse(
                adKeyword.getId(),
                adKeyword.getAdGroupId(),
                adKeyword.getDealId(),
                adKeyword.getKeywordId(),
                keywordText,
                adKeyword.getStatus().name(),
                adKeyword.getBidAmount(),
                adKeyword.getCreatedAt(),
                adKeyword.getUpdatedAt()
        );
    }
}
