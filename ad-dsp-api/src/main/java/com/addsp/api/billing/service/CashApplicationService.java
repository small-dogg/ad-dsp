package com.addsp.api.billing.service;

import com.addsp.api.billing.dto.request.ChargeCashRequest;
import com.addsp.api.billing.dto.request.DeductCashRequest;
import com.addsp.api.billing.dto.response.CashBalanceResponse;
import com.addsp.api.billing.dto.response.CashResponse;
import com.addsp.api.outbox.service.OutboxService;
import com.addsp.domain.billing.entity.Cash;
import com.addsp.domain.billing.service.CashService;
import com.addsp.domain.outbox.event.PartnerCashChargedEvent;
import com.addsp.domain.outbox.event.PartnerCashEmptyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 캐시 애플리케이션 서비스.
 * 캐시 충전/차감 오케스트레이션 및 이벤트 발행을 담당한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashApplicationService {

    private final CashService cashService;
    private final OutboxService outboxService;

    /**
     * 캐시 충전.
     */
    @Transactional
    public CashResponse charge(Long partnerId, ChargeCashRequest request) {
        Cash cash = cashService.charge(
                partnerId,
                request.type(),
                request.amount(),
                request.expiredAt()
        );

        BigDecimal totalBalance = cashService.getTotalBalance(partnerId);

        // Publish cash charged event
        outboxService.save(PartnerCashChargedEvent.of(
                partnerId,
                cash.getId(),
                cash.getType(),
                request.amount(),
                totalBalance
        ));

        return CashResponse.from(cash);
    }

    /**
     * 캐시 차감.
     */
    @Transactional
    public void deduct(Long partnerId, DeductCashRequest request) {
        BigDecimal previousBalance = cashService.deduct(partnerId, request.amount());
        BigDecimal newBalance = cashService.getTotalBalance(partnerId);

        // Publish cash empty event if balance becomes zero
        if (newBalance.compareTo(BigDecimal.ZERO) == 0 && previousBalance.compareTo(BigDecimal.ZERO) > 0) {
            outboxService.save(PartnerCashEmptyEvent.of(partnerId, previousBalance));
        }
    }

    /**
     * 잔액 조회.
     */
    public CashBalanceResponse getBalance(Long partnerId) {
        BigDecimal totalBalance = cashService.getTotalBalance(partnerId);
        return CashBalanceResponse.of(partnerId, totalBalance);
    }

    /**
     * 캐시 목록 조회.
     */
    public List<CashResponse> findByPartnerId(Long partnerId) {
        return cashService.findByPartnerId(partnerId).stream()
                .map(CashResponse::from)
                .toList();
    }
}
