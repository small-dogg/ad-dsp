package com.addsp.domain.keyword.entity;

import com.addsp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 네거티브 키워드 엔티티.
 * 글로벌하게 관리되는 금칙어로, 파트너가 광고를 등록할 때 자동 검수에 사용된다.
 */
@Entity
@Table(name = "negative_keywords")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NegativeKeyword extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String keyword;

    public NegativeKeyword(String keyword) {
        this.keyword = keyword;
    }
}
