package com.addsp.domain.keyword.entity;

import com.addsp.common.constant.KeywordMatchType;
import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "keywords")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long adGroupId;

    @Column(nullable = false)
    private String keyword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KeywordMatchType matchType;

    private BigDecimal bidAmount;

    private boolean negative;

    @Builder
    public Keyword(Long adGroupId, String keyword, KeywordMatchType matchType,
                   BigDecimal bidAmount, boolean negative) {
        this.adGroupId = adGroupId;
        this.keyword = keyword;
        this.matchType = matchType;
        this.bidAmount = bidAmount;
        this.negative = negative;
    }

    public void updateBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public void updateMatchType(KeywordMatchType matchType) {
        this.matchType = matchType;
    }

    public boolean isNegative() {
        return this.negative;
    }
}
