package com.addsp.domain.category.entity;

import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "category_targetings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryTargeting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long adGroupId;

    @Column(nullable = false)
    private Long categoryId;

    private BigDecimal bidAmount;

    private boolean excluded;

    @Builder
    public CategoryTargeting(Long adGroupId, Long categoryId, BigDecimal bidAmount, boolean excluded) {
        this.adGroupId = adGroupId;
        this.categoryId = categoryId;
        this.bidAmount = bidAmount;
        this.excluded = excluded;
    }

    public void updateBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public boolean isExcluded() {
        return this.excluded;
    }
}
