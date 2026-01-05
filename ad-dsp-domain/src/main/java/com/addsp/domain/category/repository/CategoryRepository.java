package com.addsp.domain.category.repository;

import com.addsp.domain.category.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    Optional<Category> findByCode(String code);

    List<Category> findByParentId(Long parentId);

    List<Category> findRootCategories();

    List<Category> findAll();
}
