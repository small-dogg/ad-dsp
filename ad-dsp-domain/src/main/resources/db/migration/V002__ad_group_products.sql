-- =========================
-- ad_group_products
-- =========================
create table if not exists ad_group_products (
    id           bigserial primary key,
    ad_group_id  bigint    not null,
    product_id   bigint    not null,
    partner_id   bigint    not null,

    created_at   timestamp not null,
    updated_at   timestamp not null,

    constraint fk_ad_group_products_ad_group
        foreign key (ad_group_id) references ad_groups(id),
    constraint fk_ad_group_products_partner
        foreign key (partner_id) references partners(id),
    constraint uk_ad_group_product
        unique (ad_group_id, product_id)
);

create index if not exists idx_ad_group_products_ad_group_id on ad_group_products(ad_group_id);
create index if not exists idx_ad_group_products_product_id on ad_group_products(product_id);
create index if not exists idx_ad_group_products_partner_id on ad_group_products(partner_id);
