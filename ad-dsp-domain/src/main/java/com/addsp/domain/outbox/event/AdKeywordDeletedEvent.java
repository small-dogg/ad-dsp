package com.addsp.domain.outbox.event;

import java.time.LocalDateTime;

/**
 * Event published when AdKeyword is deleted.
 */
public record AdKeywordDeletedEvent(
        Long adKeywordId,
        Long adGroupId,
        Long dealId,
        Long keywordId,
        LocalDateTime occurredAt
) implements DomainEvent {

    public static AdKeywordDeletedEvent of(Long adKeywordId, Long adGroupId, Long dealId, Long keywordId) {
        return new AdKeywordDeletedEvent(adKeywordId, adGroupId, dealId, keywordId, LocalDateTime.now());
    }

    @Override
    public String getEventType() {
        return "AD_KEYWORD_DELETED";
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
        return "rtb.adkeyword.deleted";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
