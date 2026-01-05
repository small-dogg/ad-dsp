package com.addsp.domain.adgroup.entity;

import com.addsp.common.constant.AdStatus;
import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 광고그룹 엔티티.
 * 키워드-상품 조합들을 그룹화하고 예산 및 운영 상태를 관리한다.
 */
@Entity
@Table(name = "ad_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long partnerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal dailyBudget;

    @Column(nullable = false)
    private BigDecimal totalBudget;

    @Column(nullable = false)
    private BigDecimal spentAmount;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdStatus status;

    @Builder
    public AdGroup(Long partnerId, String name, BigDecimal dailyBudget,
                   BigDecimal totalBudget, LocalDate startDate, LocalDate endDate) {
        this.partnerId = partnerId;
        this.name = name;
        this.dailyBudget = dailyBudget;
        this.totalBudget = totalBudget;
        this.spentAmount = BigDecimal.ZERO;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = AdStatus.ACTIVE;
    }

    public void activate() {
        this.status = AdStatus.ACTIVE;
    }

    public void pause() {
        this.status = AdStatus.PAUSED;
    }

    public void end() {
        this.status = AdStatus.ENDED;
    }

    public boolean isActive() {
        return this.status == AdStatus.ACTIVE;
    }

    public void addSpentAmount(BigDecimal amount) {
        this.spentAmount = this.spentAmount.add(amount);
    }

    public boolean isBudgetExhausted() {
        return this.spentAmount.compareTo(this.totalBudget) >= 0;
    }

    public void updateBudget(BigDecimal dailyBudget, BigDecimal totalBudget) {
        this.dailyBudget = dailyBudget;
        this.totalBudget = totalBudget;
    }
}
