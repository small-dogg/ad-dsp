package com.addsp.domain.ad.entity;

import com.addsp.common.constant.AdStatus;
import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ads")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ad extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long adGroupId;

    @Column(nullable = false)
    private Long dealId;

    @Column(nullable = false)
    private String productName;

    private BigDecimal bidAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdStatus status;

    @Builder
    public Ad(Long adGroupId, Long dealId, String productName, BigDecimal bidAmount) {
        this.adGroupId = adGroupId;
        this.dealId = dealId;
        this.productName = productName;
        this.bidAmount = bidAmount;
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
        this.bidAmount = bidAmount;
    }
}
