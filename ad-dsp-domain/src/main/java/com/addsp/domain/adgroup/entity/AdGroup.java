package com.addsp.domain.adgroup.entity;

import com.addsp.common.constant.AdStatus;
import com.addsp.common.constant.BillingType;
import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ad_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long campaignId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingType billingType;

    @Column(nullable = false)
    private BigDecimal defaultBidAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdStatus status;

    @Builder
    public AdGroup(Long campaignId, String name, BillingType billingType, BigDecimal defaultBidAmount) {
        this.campaignId = campaignId;
        this.name = name;
        this.billingType = billingType;
        this.defaultBidAmount = defaultBidAmount;
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

    public void updateBidAmount(BigDecimal bidAmount) {
        this.defaultBidAmount = bidAmount;
    }
}
