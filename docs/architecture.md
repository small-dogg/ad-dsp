# DSP 광고 플랫폼 아키텍처

## 개요
키워드 및 카테고리 기반 광고 플랫폼의 시스템 아키텍처를 정의한다.

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

## 2. 모듈 상세

### 2.1 ad-dsp-common
공통으로 사용되는 코드

```
ad-dsp-common/
└── src/main/java/com/addsp/common/
    ├── exception/         # 공통 예외 클래스
    ├── response/          # API 응답 포맷
    ├── util/              # 유틸리티 클래스
    └── constant/          # 상수 정의
```

**의존성:** 없음 (독립 모듈)

### 2.2 ad-dsp-domain
핵심 비즈니스 로직과 도메인 모델

```
ad-dsp-domain/
└── src/main/java/com/addsp/domain/
    ├── partner/
    │   ├── entity/        # Partner, PartnerGrade
    │   ├── repository/    # PartnerRepository (interface)
    │   └── service/       # PartnerService
    ├── campaign/
    │   ├── entity/        # Campaign, AdGroup, Ad
    │   ├── repository/
    │   └── service/
    ├── keyword/
    │   ├── entity/        # Keyword, NegativeKeyword
    │   ├── repository/
    │   └── service/
    ├── category/
    │   ├── entity/        # Category, CategoryTargeting
    │   ├── repository/
    │   └── service/
    ├── billing/
    │   ├── entity/        # Credit, Transaction, Settlement
    │   ├── repository/
    │   └── service/
    └── tracking/
        ├── entity/        # Impression, Click, Conversion
        ├── repository/
        └── service/
```

**의존성:** common

### 2.3 ad-dsp-infra
외부 시스템 연동 및 인프라 구현체

```
ad-dsp-infra/
└── src/main/java/com/addsp/infra/
    ├── persistence/
    │   ├── partner/       # PartnerJpaRepository, PartnerRepositoryImpl
    │   ├── campaign/
    │   ├── keyword/
    │   └── ...
    ├── client/
    │   └── deal/          # DealServiceClient (외부 API)
    ├── cache/             # Redis 캐시 구현
    └── config/            # 인프라 설정 (DataSource, Redis 등)
```

**의존성:** common, domain

### 2.4 ad-dsp-api
파트너(광고주)용 REST API

```
ad-dsp-api/
└── src/main/java/com/addsp/api/
    ├── controller/
    │   ├── partner/       # 파트너 관리 API
    │   ├── campaign/      # 캠페인 관리 API
    │   ├── adgroup/       # 광고그룹 관리 API
    │   ├── ad/            # 광고 관리 API
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

### 2.5 ad-dsp-serving
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

### 2.6 ad-dsp-admin
관리자용 API

```
ad-dsp-admin/
└── src/main/java/com/addsp/admin/
    ├── controller/
    │   ├── partner/       # 파트너 관리
    │   ├── category/      # 카테고리 마스터 관리
    │   ├── policy/        # 정책 관리
    │   └── dashboard/     # 운영 대시보드
    ├── dto/
    ├── security/          # 관리자 인증
    └── config/
```

**의존성:** common, domain, infra

### 2.7 ad-dsp-batch
배치 작업

```
ad-dsp-batch/
└── src/main/java/com/addsp/batch/
    ├── job/
    │   ├── settlement/    # 일별 정산
    │   ├── statistics/    # 통계 집계
    │   └── sync/          # 데이터 동기화
    ├── tasklet/
    └── config/            # Spring Batch 설정
```

**의존성:** common, domain, infra

---

## 3. 모듈 의존성

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

## 4. 레이어 아키텍처

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

## 5. 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 21 (LTS) |
| Framework | Spring Boot 4.0.1 |
| Build | Gradle 8.14+ |
| Database | PostgreSQL |
| Cache | Redis |
| Batch | Spring Batch 5.x |
| API Docs | SpringDoc OpenAPI |
| Test | JUnit 5, Mockito, Testcontainers |

---

## 6. 외부 시스템 연동

### 6.1 Deal 서비스
파트너의 상품 정보 조회

```
[ad-dsp] ──HTTP──▶ [Deal Service]
                      │
                      ▼
              상품 목록/상세 조회
```

---

## 7. 데이터 흐름

### 7.1 광고 등록 흐름

```
Partner ──▶ API ──▶ Deal 서비스에서 상품 조회
                         │
                         ▼
                    광고로 등록 (Campaign/AdGroup/Ad)
                         │
                         ▼
                    키워드/카테고리 연결
```

### 7.2 광고 서빙 흐름

```
User Request ──▶ Serving API
                     │
                     ▼
              키워드/카테고리 매칭
                     │
                     ▼
              후보 광고 필터링 (예산, 상태, 품절 등)
                     │
                     ▼
              옥션 (입찰가 × 품질점수)
                     │
                     ▼
              광고 응답 + 노출 추적
```

---

## 8. 배포 구조

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

## 9. 리팩토링 계획

현재 단일 모듈 구조에서 멀티모듈로 전환

### Phase 1: 기반 구조
1. 멀티모듈 Gradle 설정
2. common 모듈 생성
3. domain 모듈 생성

### Phase 2: 인프라 분리
4. infra 모듈 생성
5. JPA Repository 구현체 이동
6. Deal 서비스 클라이언트 구현

### Phase 3: API 분리
7. api 모듈 생성 (파트너용)
8. serving 모듈 생성 (광고 서빙용)

### Phase 4: 확장
9. admin 모듈 생성
10. batch 모듈 생성

---

## 10. 패키지 네이밍 규칙

```
com.addsp.{module}.{layer}.{domain}

예시:
- com.addsp.common.exception
- com.addsp.domain.campaign.entity
- com.addsp.domain.campaign.service
- com.addsp.infra.persistence.campaign
- com.addsp.api.controller.campaign
- com.addsp.serving.service
```
