package com.addsp.domain.category.entity;

import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private Long parentId;

    @Column(nullable = false)
    private Integer depth;

    private String path;

    @Builder
    public Category(String code, String name, Long parentId, Integer depth, String path) {
        this.code = code;
        this.name = name;
        this.parentId = parentId;
        this.depth = depth;
        this.path = path;
    }

    public boolean isRoot() {
        return this.parentId == null;
    }
}
