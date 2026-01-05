package com.addsp.domain.category.repository;

import com.addsp.domain.category.entity.CategoryTargeting;

import java.util.List;
import java.util.Optional;

public interface CategoryTargetingRepository {

    CategoryTargeting save(CategoryTargeting categoryTargeting);

    Optional<CategoryTargeting> findById(Long id);

    List<CategoryTargeting> findByAdGroupId(Long adGroupId);

    List<CategoryTargeting> findByAdGroupIdAndExcluded(Long adGroupId, boolean excluded);

    Optional<CategoryTargeting> findByAdGroupIdAndCategoryId(Long adGroupId, Long categoryId);

    void deleteById(Long id);
}
