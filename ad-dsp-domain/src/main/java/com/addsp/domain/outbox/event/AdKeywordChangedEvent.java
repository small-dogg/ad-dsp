package com.addsp.domain.outbox.event;

import com.addsp.common.constant.AdKeywordStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when AdKeyword status or bidAmount changes.
 */
public record AdKeywordChangedEvent(
        Long adKeywordId,
        Long adGroupId,
        Long dealId,
        Long keywordId,
        AdKeywordStatus status,
        BigDecimal bidAmount,
        LocalDateTime occurredAt
) implements DomainEvent {

    public static AdKeywordChangedEvent of(Long adKeywordId, Long adGroupId, Long dealId,
                                            Long keywordId, AdKeywordStatus status, BigDecimal bidAmount) {
        return new AdKeywordChangedEvent(adKeywordId, adGroupId, dealId, keywordId, status, bidAmount, LocalDateTime.now());
    }

    @Override
    public String getEventType() {
        return "AD_KEYWORD_CHANGED";
    }

    @Override
    public String getAggregateType() {
        return "AdKeyword";
    }

    @Override
    public Long getAggregateId() {
        return adKeywordId;
    }

    @Override
    public String getTopic() {
        return "rtb.adkeyword.changed";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
