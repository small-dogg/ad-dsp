-- =========================
-- 1) partners
-- =========================
create table if not exists partners (
                                        id              bigserial primary key,
                                        email           varchar(255) not null unique,
    password        varchar(255) not null,
    business_name   varchar(255) not null,
    business_number varchar(255) not null,
    contact_name    varchar(255) not null,
    contact_phone   varchar(255) not null,
    status          varchar(50)  not null, -- PartnerStatus (EnumType.STRING)

    created_at      timestamp    not null,
    updated_at      timestamp    not null
    );

-- =========================
-- 2) ad_groups
-- =========================
create table if not exists ad_groups (
                                         id           bigserial primary key,
                                         partner_id   bigint       not null,
                                         name         varchar(255) not null,
    budget_type  varchar(50)  not null default 'LIMITED', -- BudgetType (EnumType.STRING)
    daily_budget numeric(19,2) not null,
    spent_amount numeric(19,2) not null,
    mon          boolean      not null default true,
    tue          boolean      not null default true,
    wed          boolean      not null default true,
    thu          boolean      not null default true,
    fri          boolean      not null default true,
    sat          boolean      not null default true,
    sun          boolean      not null default true,
    status       varchar(50)  not null, -- AdStatus (EnumType.STRING)

    created_at   timestamp    not null,
    updated_at   timestamp    not null,

    constraint fk_ad_groups_partner
    foreign key (partner_id) references partners(id)
    );

create index if not exists idx_ad_groups_partner_id on ad_groups(partner_id);
create index if not exists idx_ad_groups_status on ad_groups(status);

-- =========================
-- 3) deals
-- =========================
create table if not exists deals (
                                     id              bigserial primary key,
                                     partner_id      bigint       not null,
                                     deal_id         bigint       not null,         -- 외부 Deal 서비스의 식별자로 보임
                                     name            varchar(255) not null,
    category1       varchar(255),
    category2       varchar(255),
    category3       varchar(255),
    synchronized_at timestamp    not null,

    created_at      timestamp    not null,
    updated_at      timestamp    not null,

    constraint fk_deals_partner
    foreign key (partner_id) references partners(id)
    );

-- (파트너별 deal_id 중복 방지 용도로 유니크를 권장)
create unique index if not exists uk_deals_partner_deal_id on deals(partner_id, deal_id);
create index if not exists idx_deals_partner_id on deals(partner_id);
create index if not exists idx_deals_deal_id on deals(deal_id);

-- =========================
-- 4) keywords
-- =========================
create table if not exists keywords (
                                        id         bigserial primary key,
                                        keyword    varchar(255) not null unique,

    created_at timestamp    not null,
    updated_at timestamp    not null
    );

-- =========================
-- 5) negative_keywords
-- =========================
create table if not exists negative_keywords (
                                                 id         bigserial primary key,
                                                 keyword    varchar(255) not null unique,

    created_at timestamp    not null,
    updated_at timestamp    not null
    );

-- =========================
-- 6) ad_keywords
-- =========================
create table if not exists ad_keywords (
                                           id          bigserial primary key,
                                           ad_group_id bigint      not null,
                                           deal_id     bigint      not null,       -- deals.id 인지 deals.deal_id 인지 엔티티만으로 확정 불가
                                           keyword_id  bigint      not null,
                                           match_type  varchar(50) not null,       -- KeywordMatchType (EnumType.STRING)
    bid_amount  numeric(19,2) not null,

    created_at  timestamp   not null,
    updated_at  timestamp   not null
    );

create index if not exists idx_ad_keywords_ad_group_id on ad_keywords(ad_group_id);
create index if not exists idx_ad_keywords_keyword_id on ad_keywords(keyword_id);
create index if not exists idx_ad_keywords_deal_id on ad_keywords(deal_id);

-- =========================
-- 7) cashes
-- =========================
create table if not exists cashes (
                                      id         bigserial primary key,
                                      partner_id bigint      not null,
                                      type       varchar(50) not null,        -- CashType (EnumType.STRING)
    amount     numeric(19,2) not null,
    balance    numeric(19,2) not null,
    expired_at timestamp,

    created_at timestamp   not null,
    updated_at timestamp   not null,

    constraint fk_cashes_partner
    foreign key (partner_id) references partners(id)
    );

create index if not exists idx_cashes_partner_id on cashes(partner_id);
create index if not exists idx_cashes_expired_at on cashes(expired_at);
create index if not exists idx_cashes_partner_type on cashes(partner_id, type);

-- =========================
-- 8) transactions
-- =========================
create table if not exists transactions (
                                            id            bigserial primary key,
                                            partner_id     bigint      not null,
                                            type           varchar(50) not null,      -- TransactionType (EnumType.STRING)
    amount         numeric(19,2) not null,
    balance_after  numeric(19,2) not null,
    description    varchar(255),
    reference_id   bigint,

    created_at     timestamp   not null,
    updated_at     timestamp   not null,

    constraint fk_transactions_partner
    foreign key (partner_id) references partners(id)
    );

create index if not exists idx_transactions_partner_id on transactions(partner_id);
create index if not exists idx_transactions_type on transactions(type);
create index if not exists idx_transactions_reference_id on transactions(reference_id);
