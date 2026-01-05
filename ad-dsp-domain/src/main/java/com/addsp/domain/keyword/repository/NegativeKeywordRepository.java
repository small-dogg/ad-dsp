package com.addsp.domain.keyword.repository;

import com.addsp.domain.keyword.entity.NegativeKeyword;

import java.util.List;
import java.util.Optional;

public interface NegativeKeywordRepository {

    NegativeKeyword save(NegativeKeyword negativeKeyword);

    Optional<NegativeKeyword> findById(Long id);

    Optional<NegativeKeyword> findByKeyword(String keyword);

    List<NegativeKeyword> findAll();

    boolean existsByKeyword(String keyword);

    void deleteById(Long id);
}
