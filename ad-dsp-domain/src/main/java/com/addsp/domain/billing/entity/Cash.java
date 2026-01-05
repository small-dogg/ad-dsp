package com.addsp.domain.billing.entity;

import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 캐시 엔티티.
 * 파트너의 충전된 캐시를 충전 단위별로 관리한다.
 * 향후 무상캐시, 반환캐시 등 타입별로 소진 우선순위가 다르게 적용될 수 있다.
 */
@Entity
@Table(name = "cashes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cash extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long partnerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CashType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal balance;

    private LocalDateTime expiredAt;

    @Builder
    public Cash(Long partnerId, CashType type, BigDecimal amount, LocalDateTime expiredAt) {
        this.partnerId = partnerId;
        this.type = type;
        this.amount = amount;
        this.balance = amount;
        this.expiredAt = expiredAt;
    }

    public void deduct(BigDecimal deductAmount) {
        if (this.balance.compareTo(deductAmount) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        this.balance = this.balance.subtract(deductAmount);
    }

    public boolean hasBalance() {
        return this.balance.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isExpired() {
        if (this.expiredAt == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(this.expiredAt);
    }

    public enum CashType {
        CHARGED,    // 충전 캐시
        FREE,       // 무상 캐시
        REFUND      // 반환 캐시
    }
}
