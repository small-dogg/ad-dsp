package com.addsp.api.infrastructure.kafka;

import com.addsp.domain.outbox.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Publishes events to Kafka.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Sends an outbox event to Kafka.
     * Topic is determined by event type.
     */
    public CompletableFuture<SendResult<String, String>> send(OutboxEvent event) {
        String topic = resolveTopicName(event);
        String key = String.valueOf(event.getAggregateId());
        String payload = event.getPayload();

        log.debug("Sending event to Kafka: topic={}, key={}", topic, key);

        return kafkaTemplate.send(topic, key, payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send event to Kafka: topic={}, key={}, error={}",
                                topic, key, ex.getMessage());
                    } else {
                        log.debug("Successfully sent event to Kafka: topic={}, key={}, offset={}",
                                topic, key, result.getRecordMetadata().offset());
                    }
                });
    }

    private String resolveTopicName(OutboxEvent event) {
        String aggregateType = event.getAggregateType().toLowerCase();
        String eventType = event.getEventType().toLowerCase().replace("_", "-");

        // Pattern: rtb.{aggregate-type}.{event-suffix}
        // e.g., AD_GROUP_STATUS_CHANGED -> rtb.adgroup.status-changed
        String eventSuffix = eventType.replace(aggregateType + "-", "");
        if (eventSuffix.startsWith("ad-")) {
            eventSuffix = eventSuffix.substring(3); // Remove "ad-" prefix
        }

        return "rtb." + aggregateType + "." + eventSuffix;
    }
}
