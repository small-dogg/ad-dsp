package com.addsp.api.adgroup.service;

import com.addsp.api.adgroup.dto.request.CreateAdGroupRequest;
import com.addsp.api.adgroup.dto.request.UpdateBudgetRequest;
import com.addsp.api.adgroup.dto.request.UpdateNameRequest;
import com.addsp.api.adgroup.dto.request.UpdateScheduleRequest;
import com.addsp.api.adgroup.dto.response.AdGroupResponse;
import com.addsp.domain.adgroup.entity.AdGroup;
import com.addsp.domain.adgroup.service.AdGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        AdGroup adGroup = adGroupService.updateBudget(
                partnerId, adGroupId, request.budgetType(), request.dailyBudget());
        return AdGroupResponse.from(adGroup);
    }

    @Transactional
    public AdGroupResponse updateSchedule(Long partnerId, Long adGroupId, UpdateScheduleRequest request) {
        AdGroup adGroup = adGroupService.updateSchedule(
                partnerId, adGroupId,
                request.mon(), request.tue(), request.wed(), request.thu(),
                request.fri(), request.sat(), request.sun());
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
}
