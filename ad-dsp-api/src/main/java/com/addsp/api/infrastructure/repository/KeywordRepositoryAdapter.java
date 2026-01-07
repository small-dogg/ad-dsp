package com.addsp.api.infrastructure.repository;

import com.addsp.domain.keyword.entity.Keyword;
import com.addsp.domain.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing domain KeywordRepository using Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class KeywordRepositoryAdapter implements KeywordRepository {

    private final KeywordJpaRepository keywordJpaRepository;

    @Override
    public Keyword save(Keyword keyword) {
        return keywordJpaRepository.save(keyword);
    }

    @Override
    public Optional<Keyword> findById(Long id) {
        return keywordJpaRepository.findById(id);
    }

    @Override
    public Optional<Keyword> findByKeyword(String keyword) {
        return keywordJpaRepository.findByKeyword(keyword);
    }

    @Override
    public List<Keyword> findByKeywordContaining(String keyword) {
        return keywordJpaRepository.findByKeywordContaining(keyword);
    }

    @Override
    public boolean existsByKeyword(String keyword) {
        return keywordJpaRepository.existsByKeyword(keyword);
    }
}
