package com.addsp.api.infrastructure.repository;

import com.addsp.domain.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Keyword entity.
 */
public interface KeywordJpaRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByKeyword(String keyword);

    List<Keyword> findByKeywordContaining(String keyword);

    boolean existsByKeyword(String keyword);
}
