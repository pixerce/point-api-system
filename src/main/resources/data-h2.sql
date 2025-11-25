
insert into point.point_type_policy (point_type, min_amount, max_cumulative_amount, cumulative_period, is_active)
values ('FREE', '1', '200000', 'TOTAL', 'Y')
, ('CASH', '1', '200000', 'TOTAL', 'Y')
, ('FREE', '1', '100000', 'ONCE', 'Y')
, ('CASH', '1', '100000', 'ONCE', 'Y')
;

insert into point.point_lifespan_policy (point_type, lifespan, unit, is_active)
values ( 'FREE', 1, 'YEAR', 'Y')
, ( 'CASH', 1, 'YEAR', 'Y')
;