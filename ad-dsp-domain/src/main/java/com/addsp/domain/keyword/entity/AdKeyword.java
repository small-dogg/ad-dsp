package com.addsp.domain.keyword.entity;

import com.addsp.common.constant.AdKeywordStatus;
import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 광고 키워드 엔티티.
 * 광고그룹 내에서 상품(Deal)과 키워드를 연결하고 입찰가를 관리한다.
 */
@Entity
@Table(name = "ad_keywords")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdKeyword extends BaseTimeEntity {

    public static final BigDecimal MIN_BID_AMOUNT = new BigDecimal("150");
    public static final BigDecimal MAX_BID_AMOUNT = new BigDecimal("99000");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long adGroupId;

    @Column(nullable = false)
    private Long dealId;

    @Column(nullable = false)
    private Long keywordId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdKeywordStatus status;

    @Column(nullable = false)
    private BigDecimal bidAmount;

    @Builder
    public AdKeyword(Long adGroupId, Long dealId, Long keywordId,
                     AdKeywordStatus status, BigDecimal bidAmount) {
        this.adGroupId = adGroupId;
        this.dealId = dealId;
        this.keywordId = keywordId;
        this.status = status != null ? status : AdKeywordStatus.PENDING;
        this.bidAmount = bidAmount;
    }

    public void updateBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public void updateStatus(AdKeywordStatus status) {
        this.status = status;
    }

    public void approve() {
        this.status = AdKeywordStatus.APPROVED;
    }

    public void reject() {
        this.status = AdKeywordStatus.REJECTED;
    }

    public void deactivate() {
        this.status = AdKeywordStatus.INACTIVE;
    }

    public boolean isActive() {
        return this.status == AdKeywordStatus.APPROVED;
    }
}
