package com.addsp.domain.keyword.entity;

import com.addsp.common.constant.KeywordMatchType;
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
    private KeywordMatchType matchType;

    @Column(nullable = false)
    private BigDecimal bidAmount;

    @Builder
    public AdKeyword(Long adGroupId, Long dealId, Long keywordId,
                     KeywordMatchType matchType, BigDecimal bidAmount) {
        this.adGroupId = adGroupId;
        this.dealId = dealId;
        this.keywordId = keywordId;
        this.matchType = matchType;
        this.bidAmount = bidAmount;
    }

    public void updateBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public void updateMatchType(KeywordMatchType matchType) {
        this.matchType = matchType;
    }
}
