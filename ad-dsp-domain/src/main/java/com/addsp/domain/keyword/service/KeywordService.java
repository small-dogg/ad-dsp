package com.addsp.domain.keyword.service;

import com.addsp.common.exception.BusinessException;
import com.addsp.common.exception.ErrorCode;
import com.addsp.domain.keyword.entity.Keyword;
import com.addsp.domain.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 키워드 도메인 서비스.
 * 키워드는 관리자 승인 시 자동으로 등록된다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {

    private final KeywordRepository keywordRepository;

    /**
     * 키워드 텍스트로 키워드를 조회하거나, 없으면 새로 생성한다.
     */
    @Transactional
    public Keyword findOrCreate(String keywordText) {
        return keywordRepository.findByKeyword(keywordText)
                .orElseGet(() -> keywordRepository.save(new Keyword(keywordText)));
    }

    public Keyword findById(Long id) {
        return keywordRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEYWORD_NOT_FOUND));
    }

    public Keyword findByKeyword(String keyword) {
        return keywordRepository.findByKeyword(keyword)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEYWORD_NOT_FOUND));
    }
}
