package com.addsp.domain.keyword.repository;

import com.addsp.domain.keyword.entity.Keyword;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository {

    Keyword save(Keyword keyword);

    Optional<Keyword> findById(Long id);

    List<Keyword> findByAdGroupId(Long adGroupId);

    List<Keyword> findByAdGroupIdAndNegative(Long adGroupId, boolean negative);

    List<Keyword> findByKeywordContaining(String keyword);

    boolean existsByAdGroupIdAndKeyword(Long adGroupId, String keyword);

    void deleteById(Long id);
}
