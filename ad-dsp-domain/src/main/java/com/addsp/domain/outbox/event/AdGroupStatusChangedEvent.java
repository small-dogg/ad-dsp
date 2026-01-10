package com.addsp.domain.outbox.event;

import com.addsp.common.constant.AdStatus;

import java.time.LocalDateTime;

/**
 * Event published when AdGroup status changes (ON/OFF toggle).
 */
public record AdGroupStatusChangedEvent(
        Long adGroupId,
        Long partnerId,
        AdStatus previousStatus,
        AdStatus newStatus,
        LocalDateTime occurredAt
) implements DomainEvent {

    public static AdGroupStatusChangedEvent of(Long adGroupId, Long partnerId,
                                                AdStatus previousStatus, AdStatus newStatus) {
        return new AdGroupStatusChangedEvent(adGroupId, partnerId, previousStatus, newStatus, LocalDateTime.now());
    }

    @Override
    public String getEventType() {
        return "AD_GROUP_STATUS_CHANGED";
    }

    @Override
    public String getAggregateType() {
        return "AdGroup";
    }

    @Override
    public Long getAggregateId() {
        return adGroupId;
    }

    @Override
    public String getTopic() {
        return "rtb.adgroup.status-changed";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
