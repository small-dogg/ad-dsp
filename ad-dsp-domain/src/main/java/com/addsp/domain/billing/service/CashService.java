package com.addsp.domain.billing.service;

import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.billing.entity.Cash;
import com.addsp.domain.billing.repository.CashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 캐시 도메인 서비스.
 * 파트너의 캐시 충전, 차감, 잔액 조회를 담당한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashService {

    private final CashRepository cashRepository;

    /**
     * 캐시 충전.
     */
    @Transactional
    public Cash charge(Long partnerId, Cash.CashType type, BigDecimal amount, LocalDateTime expiredAt) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.INVALID_CHARGE_AMOUNT);
        }

        Cash cash = Cash.builder()
                .partnerId(partnerId)
                .type(type)
                .amount(amount)
                .expiredAt(expiredAt)
                .build();

        return cashRepository.save(cash);
    }

    /**
     * 캐시 차감. 만료일이 가까운 캐시부터 차감한다.
     * @return 차감 전 잔액
     */
    @Transactional
    public BigDecimal deduct(Long partnerId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.INVALID_CHARGE_AMOUNT);
        }

        BigDecimal previousBalance = getTotalBalance(partnerId);

        if (previousBalance.compareTo(amount) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        List<Cash> activeCashes = cashRepository.findActiveByPartnerIdOrderByExpiredAtAsc(partnerId);
        BigDecimal remaining = amount;

        for (Cash cash : activeCashes) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal deductible = cash.getBalance().min(remaining);
            cash.deduct(deductible);
            remaining = remaining.subtract(deductible);
        }

        return previousBalance;
    }

    /**
     * 파트너의 총 잔액 조회.
     */
    public BigDecimal getTotalBalance(Long partnerId) {
        List<Cash> activeCashes = cashRepository.findActiveByPartnerIdOrderByExpiredAtAsc(partnerId);
        return activeCashes.stream()
                .map(Cash::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 파트너의 캐시 목록 조회.
     */
    public List<Cash> findByPartnerId(Long partnerId) {
        return cashRepository.findByPartnerId(partnerId);
    }
}
