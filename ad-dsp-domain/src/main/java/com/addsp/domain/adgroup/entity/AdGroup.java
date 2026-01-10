package com.addsp.domain.adgroup.entity;

import com.addsp.common.constant.AdStatus;
import com.addsp.common.constant.BudgetType;
import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 광고그룹 엔티티.
 * 키워드-상품 조합들을 그룹화하고 예산 및 운영 상태를 관리한다.
 */
@Entity
@Table(name = "ad_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdGroup extends BaseTimeEntity {

    private static final BigDecimal DEFAULT_DAILY_BUDGET = new BigDecimal("99000");
    public static final BigDecimal MIN_DAILY_BUDGET = new BigDecimal("10000");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long partnerId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetType budgetType;

    @Column(nullable = false)
    private BigDecimal dailyBudget;

    @Column(nullable = false)
    private BigDecimal spentAmount;

    // 요일별 경매 참여 여부
    @Column(nullable = false)
    private Boolean mon;

    @Column(nullable = false)
    private Boolean tue;

    @Column(nullable = false)
    private Boolean wed;

    @Column(nullable = false)
    private Boolean thu;

    @Column(nullable = false)
    private Boolean fri;

    @Column(nullable = false)
    private Boolean sat;

    @Column(nullable = false)
    private Boolean sun;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdStatus status;

    @Builder
    public AdGroup(Long partnerId, String name, BudgetType budgetType, BigDecimal dailyBudget,
                   Boolean mon, Boolean tue, Boolean wed, Boolean thu,
                   Boolean fri, Boolean sat, Boolean sun) {
        this.partnerId = partnerId;
        this.name = name;
        this.budgetType = budgetType != null ? budgetType : BudgetType.LIMITED;
        this.dailyBudget = this.budgetType == BudgetType.UNLIMITED
                ? BigDecimal.ZERO
                : (dailyBudget != null ? dailyBudget : DEFAULT_DAILY_BUDGET);
        this.spentAmount = BigDecimal.ZERO;
        this.mon = mon != null ? mon : true;
        this.tue = tue != null ? tue : true;
        this.wed = wed != null ? wed : true;
        this.thu = thu != null ? thu : true;
        this.fri = fri != null ? fri : true;
        this.sat = sat != null ? sat : true;
        this.sun = sun != null ? sun : true;
        this.status = AdStatus.PENDING;
    }

    public void activate() {
        this.status = AdStatus.ACTIVE;
    }

    public void stop() {
        this.status = AdStatus.STOPPED;
    }

    public void end() {
        this.status = AdStatus.ENDED;
    }

    public void pending() {
        this.status = AdStatus.PENDING;
    }

    public boolean isActive() {
        return this.status == AdStatus.ACTIVE;
    }

    public boolean isStopped() {
        return this.status == AdStatus.STOPPED;
    }

    public boolean canDelete() {
        return this.status == AdStatus.STOPPED;
    }

    public void addSpentAmount(BigDecimal amount) {
        this.spentAmount = this.spentAmount.add(amount);
    }

    public void updateBudget(BudgetType budgetType, BigDecimal dailyBudget) {
        this.budgetType = budgetType;
        this.dailyBudget = budgetType == BudgetType.UNLIMITED ? BigDecimal.ZERO : dailyBudget;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateSchedule(Boolean mon, Boolean tue, Boolean wed, Boolean thu,
                               Boolean fri, Boolean sat, Boolean sun) {
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }
}
