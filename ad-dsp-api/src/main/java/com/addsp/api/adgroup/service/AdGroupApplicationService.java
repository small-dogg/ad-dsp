package com.addsp.api.adgroup.service;

import com.addsp.api.adgroup.dto.request.CreateAdGroupRequest;
import com.addsp.api.adgroup.dto.request.UpdateBudgetRequest;
import com.addsp.api.adgroup.dto.request.UpdateNameRequest;
import com.addsp.api.adgroup.dto.request.UpdateScheduleRequest;
import com.addsp.api.adgroup.dto.response.AdGroupResponse;
import com.addsp.api.outbox.service.OutboxService;
import com.addsp.common.constant.AdStatus;
import com.addsp.common.constant.BudgetType;
import com.addsp.domain.adgroup.entity.AdGroup;
import com.addsp.domain.adgroup.service.AdGroupService;
import com.addsp.domain.outbox.event.AdGroupBudgetChangedEvent;
import com.addsp.domain.outbox.event.AdGroupScheduleChangedEvent;
import com.addsp.domain.outbox.event.AdGroupStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 광고그룹 애플리케이션 서비스.
 * API 계층과 도메인 계층 사이의 오케스트레이션을 담당한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdGroupApplicationService {

    private final AdGroupService adGroupService;
    private final OutboxService outboxService;

    @Transactional
    public AdGroupResponse create(Long partnerId, CreateAdGroupRequest request) {
        AdGroup adGroup = adGroupService.create(
                partnerId,
                request.name(),
                request.budgetType(),
                request.dailyBudget(),
                request.mon(),
                request.tue(),
                request.wed(),
                request.thu(),
                request.fri(),
                request.sat(),
                request.sun()
        );
        return AdGroupResponse.from(adGroup);
    }

    public AdGroupResponse findById(Long partnerId, Long adGroupId) {
        AdGroup adGroup = adGroupService.findByIdAndPartnerId(adGroupId, partnerId);
        return AdGroupResponse.from(adGroup);
    }

    public List<AdGroupResponse> findAll(Long partnerId) {
        return adGroupService.findAllByPartnerId(partnerId).stream()
                .map(AdGroupResponse::from)
                .toList();
    }

    @Transactional
    public AdGroupResponse updateBudget(Long partnerId, Long adGroupId, UpdateBudgetRequest request) {
        // Capture previous state
        AdGroup beforeUpdate = adGroupService.findByIdAndPartnerId(adGroupId, partnerId);
        BudgetType previousBudgetType = beforeUpdate.getBudgetType();
        BigDecimal previousDailyBudget = beforeUpdate.getDailyBudget();

        AdGroup adGroup = adGroupService.updateBudget(
                partnerId, adGroupId, request.budgetType(), request.dailyBudget());

        // Publish event if budget changed
        if (!previousBudgetType.equals(adGroup.getBudgetType()) ||
                previousDailyBudget.compareTo(adGroup.getDailyBudget()) != 0) {
            outboxService.save(AdGroupBudgetChangedEvent.of(
                    adGroup.getId(),
                    partnerId,
                    adGroup.getBudgetType(),
                    adGroup.getDailyBudget()
            ));
        }

        return AdGroupResponse.from(adGroup);
    }

    @Transactional
    public AdGroupResponse updateSchedule(Long partnerId, Long adGroupId, UpdateScheduleRequest request) {
        AdGroup adGroup = adGroupService.updateSchedule(
                partnerId, adGroupId,
                request.mon(), request.tue(), request.wed(), request.thu(),
                request.fri(), request.sat(), request.sun());

        // Publish schedule changed event
        outboxService.save(AdGroupScheduleChangedEvent.of(
                adGroup.getId(),
                partnerId,
                adGroup.getMon(),
                adGroup.getTue(),
                adGroup.getWed(),
                adGroup.getThu(),
                adGroup.getFri(),
                adGroup.getSat(),
                adGroup.getSun()
        ));

        return AdGroupResponse.from(adGroup);
    }

    @Transactional
    public AdGroupResponse updateName(Long partnerId, Long adGroupId, UpdateNameRequest request) {
        AdGroup adGroup = adGroupService.updateName(partnerId, adGroupId, request.name());
        return AdGroupResponse.from(adGroup);
    }

    @Transactional
    public void delete(Long partnerId, Long adGroupId) {
        adGroupService.delete(partnerId, adGroupId);
    }

    @Transactional
    public AdGroupResponse toggleStatus(Long partnerId, Long adGroupId, boolean enabled) {
        // Capture previous status
        AdGroup beforeToggle = adGroupService.findByIdAndPartnerId(adGroupId, partnerId);
        AdStatus previousStatus = beforeToggle.getStatus();

        AdGroup adGroup = adGroupService.toggleStatus(partnerId, adGroupId, enabled);

        // Publish event if status changed
        if (!previousStatus.equals(adGroup.getStatus())) {
            outboxService.save(AdGroupStatusChangedEvent.of(
                    adGroup.getId(),
                    partnerId,
                    previousStatus,
                    adGroup.getStatus()
            ));
        }

        return AdGroupResponse.from(adGroup);
    }
}
