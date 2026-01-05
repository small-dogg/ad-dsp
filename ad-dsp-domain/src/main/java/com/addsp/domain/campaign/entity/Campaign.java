package com.addsp.domain.campaign.entity;

import com.addsp.common.constant.CampaignStatus;
import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "campaigns")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Campaign extends BaseTimeEntity {

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
    private CampaignStatus status;

    @Builder
    public Campaign(Long partnerId, String name, BigDecimal dailyBudget,
                    BigDecimal totalBudget, LocalDate startDate, LocalDate endDate) {
        this.partnerId = partnerId;
        this.name = name;
        this.dailyBudget = dailyBudget;
        this.totalBudget = totalBudget;
        this.spentAmount = BigDecimal.ZERO;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = CampaignStatus.READY;
    }

    public void activate() {
        this.status = CampaignStatus.ACTIVE;
    }

    public void pause() {
        this.status = CampaignStatus.PAUSED;
    }

    public void end() {
        this.status = CampaignStatus.ENDED;
    }

    public void addSpentAmount(BigDecimal amount) {
        this.spentAmount = this.spentAmount.add(amount);
    }

    public boolean isActive() {
        return this.status == CampaignStatus.ACTIVE;
    }

    public boolean isBudgetExhausted() {
        return this.spentAmount.compareTo(this.totalBudget) >= 0;
    }
}
