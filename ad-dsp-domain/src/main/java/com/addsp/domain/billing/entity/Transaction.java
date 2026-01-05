package com.addsp.domain.billing.entity;

import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long partnerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal balanceAfter;

    private String description;

    private Long referenceId;

    @Builder
    public Transaction(Long partnerId, TransactionType type, BigDecimal amount,
                       BigDecimal balanceAfter, String description, Long referenceId) {
        this.partnerId = partnerId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.referenceId = referenceId;
    }

    public enum TransactionType {
        CHARGE,      // 충전
        SPEND,       // 광고비 차감
        REFUND,      // 환불
        ADJUSTMENT   // 조정
    }
}
