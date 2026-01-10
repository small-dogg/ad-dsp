package com.addsp.domain.outbox.event;

import java.time.LocalDateTime;

/**
 * Event published when AdGroup schedule changes.
 */
public record AdGroupScheduleChangedEvent(
        Long adGroupId,
        Long partnerId,
        Boolean mon,
        Boolean tue,
        Boolean wed,
        Boolean thu,
        Boolean fri,
        Boolean sat,
        Boolean sun,
        LocalDateTime occurredAt
) implements DomainEvent {

    public static AdGroupScheduleChangedEvent of(Long adGroupId, Long partnerId,
                                                  Boolean mon, Boolean tue, Boolean wed,
                                                  Boolean thu, Boolean fri, Boolean sat, Boolean sun) {
        return new AdGroupScheduleChangedEvent(adGroupId, partnerId, mon, tue, wed, thu, fri, sat, sun, LocalDateTime.now());
    }

    @Override
    public String getEventType() {
        return "AD_GROUP_SCHEDULE_CHANGED";
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
        return "rtb.adgroup.schedule-changed";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
