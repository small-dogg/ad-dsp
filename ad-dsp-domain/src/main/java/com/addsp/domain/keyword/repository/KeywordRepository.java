package com.addsp.domain.keyword.repository;

import com.addsp.domain.keyword.entity.Keyword;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository {

    Keyword save(Keyword keyword);

    Optional<Keyword> findById(Long id);

    Optional<Keyword> findByKeyword(String keyword);

    List<Keyword> findByKeywordContaining(String keyword);

    boolean existsByKeyword(String keyword);
}
