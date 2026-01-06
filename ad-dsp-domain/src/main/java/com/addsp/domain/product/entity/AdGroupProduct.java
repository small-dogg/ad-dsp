package com.addsp.domain.product.entity;

import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 광고그룹-상품 연결 엔티티.
 * 광고그룹에 등록된 상품 정보를 관리한다.
 */
@Entity
@Table(name = "ad_group_products",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_ad_group_product",
                columnNames = {"ad_group_id", "product_id"}
        ))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdGroupProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad_group_id", nullable = false)
    private Long adGroupId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "partner_id", nullable = false)
    private Long partnerId;

    @Builder
    public AdGroupProduct(Long adGroupId, Long productId, Long partnerId) {
        this.adGroupId = adGroupId;
        this.productId = productId;
        this.partnerId = partnerId;
    }
}
