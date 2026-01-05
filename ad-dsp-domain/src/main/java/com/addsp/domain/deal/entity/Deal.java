package com.addsp.domain.deal.entity;

import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Deal 서비스로부터 동기화된 상품 정보를 저장하는 엔티티.
 * 파트너가 광고로 등록할 수 있는 상품 목록을 관리한다.
 */
@Entity
@Table(name = "deals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long partnerId;

    @Column(nullable = false)
    private Long dealId;

    @Column(nullable = false)
    private String name;

    private String category1;

    private String category2;

    private String category3;

    @Column(nullable = false)
    private LocalDateTime synchronizedAt;

    @Builder
    public Deal(Long partnerId, Long dealId, String name,
                String category1, String category2, String category3) {
        this.partnerId = partnerId;
        this.dealId = dealId;
        this.name = name;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.synchronizedAt = LocalDateTime.now();
    }

    public void synchronize(String name, String category1, String category2, String category3) {
        this.name = name;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.synchronizedAt = LocalDateTime.now();
    }
}
