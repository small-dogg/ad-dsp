package com.addsp.domain.billing.repository;

import com.addsp.domain.billing.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    List<Transaction> findByPartnerId(Long partnerId);

    List<Transaction> findByPartnerIdAndCreatedAtBetween(Long partnerId, LocalDateTime start, LocalDateTime end);
}
