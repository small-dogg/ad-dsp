alter table public.ad_keywords drop column match_type;
alter table public.ad_keywords add status varchar(20) not null;