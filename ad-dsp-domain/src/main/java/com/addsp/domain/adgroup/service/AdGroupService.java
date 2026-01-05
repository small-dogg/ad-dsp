package com.addsp.domain.adgroup.service;

import com.addsp.common.constant.BudgetType;
import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.adgroup.entity.AdGroup;
import com.addsp.domain.adgroup.repository.AdGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 광고그룹 도메인 서비스.
 * 광고그룹 생성, 조회, 수정, 삭제 등 핵심 비즈니스 로직을 담당한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdGroupService {

    private final AdGroupRepository adGroupRepository;

    @Transactional
    public AdGroup create(Long partnerId, String name, BudgetType budgetType, BigDecimal dailyBudget,
                          Boolean mon, Boolean tue, Boolean wed, Boolean thu,
                          Boolean fri, Boolean sat, Boolean sun) {
        validateNameDuplicate(partnerId, name);
        validateBudget(budgetType, dailyBudget);

        AdGroup adGroup = AdGroup.builder()
                .partnerId(partnerId)
                .name(name)
                .budgetType(budgetType)
                .dailyBudget(dailyBudget)
                .mon(mon)
                .tue(tue)
                .wed(wed)
                .thu(thu)
                .fri(fri)
                .sat(sat)
                .sun(sun)
                .build();

        return adGroupRepository.save(adGroup);
    }

    public AdGroup findById(Long id) {
        return adGroupRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.AD_GROUP_NOT_FOUND));
    }

    public AdGroup findByIdAndPartnerId(Long id, Long partnerId) {
        AdGroup adGroup = findById(id);
        if (!adGroup.getPartnerId().equals(partnerId)) {
            throw new BusinessException(ErrorCode.AD_GROUP_NOT_FOUND);
        }
        return adGroup;
    }

    public List<AdGroup> findAllByPartnerId(Long partnerId) {
        return adGroupRepository.findByPartnerId(partnerId);
    }

    @Transactional
    public AdGroup updateBudget(Long partnerId, Long adGroupId, BudgetType budgetType, BigDecimal dailyBudget) {
        validateBudget(budgetType, dailyBudget);

        AdGroup adGroup = findByIdAndPartnerId(adGroupId, partnerId);
        adGroup.updateBudget(budgetType, dailyBudget);
        return adGroup;
    }

    @Transactional
    public AdGroup updateSchedule(Long partnerId, Long adGroupId,
                                   Boolean mon, Boolean tue, Boolean wed, Boolean thu,
                                   Boolean fri, Boolean sat, Boolean sun) {
        AdGroup adGroup = findByIdAndPartnerId(adGroupId, partnerId);
        adGroup.updateSchedule(mon, tue, wed, thu, fri, sat, sun);
        return adGroup;
    }

    @Transactional
    public AdGroup updateName(Long partnerId, Long adGroupId, String name) {
        AdGroup adGroup = findByIdAndPartnerId(adGroupId, partnerId);
        validateNameDuplicateExcludeSelf(partnerId, name, adGroupId);
        adGroup.updateName(name);
        return adGroup;
    }

    @Transactional
    public void delete(Long partnerId, Long adGroupId) {
        AdGroup adGroup = findByIdAndPartnerId(adGroupId, partnerId);
        if (!adGroup.canDelete()) {
            throw new BusinessException(ErrorCode.AD_GROUP_CANNOT_DELETE);
        }
        adGroupRepository.delete(adGroup);
    }

    private void validateNameDuplicate(Long partnerId, String name) {
        if (adGroupRepository.existsByPartnerIdAndName(partnerId, name)) {
            throw new BusinessException(ErrorCode.AD_GROUP_NAME_DUPLICATE);
        }
    }

    private void validateNameDuplicateExcludeSelf(Long partnerId, String name, Long adGroupId) {
        if (adGroupRepository.existsByPartnerIdAndNameAndIdNot(partnerId, name, adGroupId)) {
            throw new BusinessException(ErrorCode.AD_GROUP_NAME_DUPLICATE);
        }
    }

    private void validateBudget(BudgetType budgetType, BigDecimal dailyBudget) {
        if (budgetType == BudgetType.LIMITED) {
            if (dailyBudget == null || dailyBudget.compareTo(AdGroup.MIN_DAILY_BUDGET) < 0) {
                throw new BusinessException(ErrorCode.AD_GROUP_INVALID_BUDGET);
            }
        }
    }
}
