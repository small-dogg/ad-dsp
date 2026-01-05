# DSP 광고 플랫폼 아키텍처

## 개요
키워드 기반 광고 플랫폼의 시스템 아키텍처를 정의한다.
파트너가 Deal 서비스의 상품을 키워드와 연결하여 광고로 등록하고, 키워드 검색 및 카테고리 탐색 시 광고를 노출한다.

---

## 1. 멀티모듈 구조

```
ad-dsp/
├── ad-dsp-common          # 공통 유틸, 예외, DTO
├── ad-dsp-domain          # 도메인 엔티티, 비즈니스 로직
├── ad-dsp-infra           # 인프라 구현체
├── ad-dsp-api             # 파트너용 REST API
├── ad-dsp-serving         # 광고 서빙 API
├── ad-dsp-admin           # 관리자용 API
└── ad-dsp-batch           # 배치 작업
```

---

## 2. 엔티티 구조

### 2.1 엔티티 관계도

```
Partner (파트너)
  ├── AdGroup (광고그룹) [1:N]
  │     └── AdKeyword (키워드-상품 연결) [1:N]
  │           ├── → Deal (상품)
  │           └── → Keyword (키워드 마스터)
  ├── Deal (상품) [1:N]
  ├── Cash (캐시) [1:N]
  └── Transaction (거래내역) [1:N]

Keyword (키워드 마스터) - 독립
NegativeKeyword (글로벌 금칙어) - 독립
```

### 2.2 엔티티 상세

| 엔티티 | 테이블 | 설명 |
|--------|--------|------|
| **Partner** | partners | 파트너(광고주) 정보 |
| **AdGroup** | ad_groups | 광고그룹. 예산 및 운영 상태 관리 |
| **Deal** | deals | Deal 서비스에서 동기화된 상품 |
| **Keyword** | keywords | 키워드 마스터 |
| **AdKeyword** | ad_keywords | 광고그룹 내 상품-키워드 연결 및 입찰가 |
| **NegativeKeyword** | negative_keywords | 글로벌 금칙어 (광고 등록 시 자동 검수용) |
| **Cash** | cashes | 파트너 캐시 (충전 단위별 관리) |
| **Transaction** | transactions | 거래 내역 |

### 2.3 주요 엔티티 필드

**Partner**
```
id, email, password, businessName, businessNumber, contactName, contactPhone, status
```

**AdGroup**
```
id, partnerId, name, dailyBudget, totalBudget, spentAmount, startDate, endDate, status
```

**Deal**
```
id, partnerId, dealId, name, category1, category2, category3, synchronizedAt
```

**AdKeyword**
```
id, adGroupId, dealId, keywordId, matchType, bidAmount
```

**Cash**
```
id, partnerId, type(CHARGED/FREE/REFUND), amount, balance, expiredAt
```

---

## 3. 모듈 상세

### 3.1 ad-dsp-common
공통으로 사용되는 코드

```
ad-dsp-common/
└── src/main/java/com/addsp/common/
    ├── exception/         # 공통 예외 클래스
    ├── response/          # API 응답 포맷
    ├── util/              # 유틸리티 클래스
    └── constant/          # 상수 정의 (AdStatus, PartnerStatus, KeywordMatchType)
```

**의존성:** 없음 (독립 모듈)

### 3.2 ad-dsp-domain
핵심 비즈니스 로직과 도메인 모델

```
ad-dsp-domain/
└── src/main/java/com/addsp/domain/
    ├── common/            # BaseTimeEntity
    ├── partner/
    │   ├── entity/        # Partner
    │   ├── repository/
    │   └── service/
    ├── adgroup/
    │   ├── entity/        # AdGroup
    │   ├── repository/
    │   └── service/
    ├── deal/
    │   ├── entity/        # Deal
    │   ├── repository/
    │   └── service/
    ├── keyword/
    │   ├── entity/        # Keyword, AdKeyword, NegativeKeyword
    │   ├── repository/
    │   └── service/
    └── billing/
        ├── entity/        # Cash, Transaction
        ├── repository/
        └── service/
```

**의존성:** common

### 3.3 ad-dsp-infra
외부 시스템 연동 및 인프라 구현체

```
ad-dsp-infra/
└── src/main/java/com/addsp/infra/
    ├── persistence/
    │   ├── partner/       # PartnerJpaRepository, PartnerRepositoryImpl
    │   ├── adgroup/
    │   ├── deal/
    │   ├── keyword/
    │   └── billing/
    ├── client/
    │   └── deal/          # DealServiceClient (외부 API)
    ├── cache/             # Redis 캐시 구현
    └── config/            # 인프라 설정 (DataSource, Redis 등)
```

**의존성:** common, domain

### 3.4 ad-dsp-api
파트너(광고주)용 REST API

```
ad-dsp-api/
└── src/main/java/com/addsp/api/
    ├── controller/
    │   ├── partner/       # 파트너 관리 API
    │   ├── adgroup/       # 광고그룹 관리 API
    │   ├── deal/          # 상품 조회 API
    │   ├── keyword/       # 키워드 관리 API
    │   ├── report/        # 리포팅 API
    │   └── billing/       # 정산 API
    ├── dto/
    │   ├── request/
    │   └── response/
    ├── security/          # 인증/인가
    └── config/            # API 설정
```

**의존성:** common, domain, infra

### 3.5 ad-dsp-serving
광고 서빙 전용 API (고성능)

```
ad-dsp-serving/
└── src/main/java/com/addsp/serving/
    ├── controller/
    │   └── AdServingController
    ├── service/
    │   ├── AdSelectionService    # 광고 선택
    │   ├── AuctionService        # 옥션 처리
    │   └── RankingService        # 랭킹 계산
    ├── dto/
    ├── cache/             # 서빙용 캐시 로직
    └── config/
```

**의존성:** common, domain, infra

### 3.6 ad-dsp-admin
관리자용 API

```
ad-dsp-admin/
└── src/main/java/com/addsp/admin/
    ├── controller/
    │   ├── partner/       # 파트너 관리
    │   ├── keyword/       # 금칙어 관리
    │   ├── policy/        # 정책 관리
    │   └── dashboard/     # 운영 대시보드
    ├── dto/
    ├── security/          # 관리자 인증
    └── config/
```

**의존성:** common, domain, infra

### 3.7 ad-dsp-batch
배치 작업

```
ad-dsp-batch/
└── src/main/java/com/addsp/batch/
    ├── job/
    │   ├── settlement/    # 일별 정산
    │   ├── statistics/    # 통계 집계
    │   └── sync/          # Deal 서비스 동기화
    ├── tasklet/
    └── config/            # Spring Batch 설정
```

**의존성:** common, domain, infra

---

## 4. 모듈 의존성

```
                    ┌─────────────┐
                    │   common    │
                    └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │   domain    │
                    └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │    infra    │
                    └──────┬──────┘
                           │
        ┌──────────┬───────┼───────┬──────────┐
        │          │       │       │          │
   ┌────▼───┐ ┌────▼───┐ ┌─▼──┐ ┌──▼───┐ ┌────▼───┐
   │  api   │ │serving │ │admin│ │batch │ │  ...   │
   └────────┘ └────────┘ └────┘ └──────┘ └────────┘
```

---

## 5. 레이어 아키텍처

각 API 모듈 내부는 다음 레이어 구조를 따른다.

```
┌─────────────────────────────────────────┐
│           Controller Layer              │  ← HTTP 요청/응답 처리
├─────────────────────────────────────────┤
│           Application Layer             │  ← 유스케이스 조합, 트랜잭션
├─────────────────────────────────────────┤
│            Domain Layer                 │  ← 비즈니스 로직, 엔티티
├─────────────────────────────────────────┤
│         Infrastructure Layer            │  ← DB, 외부 API, 캐시
└─────────────────────────────────────────┘
```

---

## 6. 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 21 (LTS) |
| Framework | Spring Boot 4.0.1 |
| Build | Gradle 8.14+ |
| Database | PostgreSQL |
| ORM | JPA + QueryDSL |
| Cache | Redis |
| Batch | Spring Batch 5.x |
| API Docs | SpringDoc OpenAPI |
| Test | JUnit 5, Mockito, Testcontainers |

---

## 7. 외부 시스템 연동

### 7.1 Deal 서비스
파트너의 상품 정보 조회 및 동기화

```
[ad-dsp] ──HTTP──▶ [Deal Service]
                      │
                      ▼
              상품 목록/상세 조회
              (dealId, name, category1~3)
```

---

## 8. 데이터 흐름

### 8.1 광고 등록 흐름

```
Partner ──▶ API ──▶ Deal 서비스에서 상품 조회
                         │
                         ▼
                    Deal 테이블에 동기화
                         │
                         ▼
                    광고그룹 생성 (예산, 기간 설정)
                         │
                         ▼
                    AdKeyword 등록 (상품-키워드 연결, 입찰가)
```

### 8.2 광고 서빙 흐름

```
User Request (키워드 검색)
         │
         ▼
    Serving API
         │
         ▼
    Keyword 매칭 → AdKeyword 조회
         │
         ▼
    필터링 (예산 소진, 상태, 품절 등)
         │
         ▼
    옥션 (bidAmount 기반)
         │
         ▼
    광고 응답 + 노출 추적
```

### 8.3 카테고리 광고 노출

```
User Request (카테고리 진입)
         │
         ▼
    Deal.category1~3 매칭
         │
         ▼
    해당 상품의 AdKeyword 조회
         │
         ▼
    옥션 → 광고 응답
```

---

## 9. 배포 구조

```
┌─────────────────────────────────────────────────────────┐
│                      Load Balancer                       │
└─────────────┬───────────────────────────┬───────────────┘
              │                           │
      ┌───────▼───────┐           ┌───────▼───────┐
      │   API Server  │           │Serving Server │
      │  (ad-dsp-api) │           │(ad-dsp-serving)│
      └───────┬───────┘           └───────┬───────┘
              │                           │
              └─────────────┬─────────────┘
                            │
              ┌─────────────▼─────────────┐
              │     PostgreSQL / Redis     │
              └───────────────────────────┘
```

- **API Server**: 파트너 대상, 일반적인 트래픽
- **Serving Server**: 광고 요청 대상, 고성능/저지연 필요, 독립 스케일링

---

## 10. 리팩토링 계획

### Phase 1: 기반 구조 ✅
1. 멀티모듈 Gradle 설정
2. common 모듈 생성
3. domain 모듈 생성

### Phase 2: 인프라 분리
4. infra 모듈 생성
5. JPA Repository 구현체 이동
6. Deal 서비스 클라이언트 구현
7. 환경별 설정 파일 구성 (local, dev, prod)

### Phase 3: API 분리
8. api 모듈 생성 (파트너용)
9. serving 모듈 생성 (광고 서빙용)

### Phase 4: 확장
10. admin 모듈 생성
11. batch 모듈 생성

---

## 11. 패키지 네이밍 규칙

```
com.addsp.{module}.{layer}.{domain}

예시:
- com.addsp.common.exception
- com.addsp.domain.adgroup.entity
- com.addsp.domain.keyword.service
- com.addsp.infra.persistence.deal
- com.addsp.api.controller.adgroup
- com.addsp.serving.service
```
