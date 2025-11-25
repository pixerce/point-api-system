package com.point.system.domain.entity;

import com.point.system.domain.valueobject.CumulativePeriod;
import com.point.system.domain.valueobject.PointType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@RequiredArgsConstructor
public class PointTypePolicy {
    private final Long minAmount;
    private final Long maxCumulativeAmount;
    private final PointType poinType;
    private final CumulativePeriod cumulativePeriod;
}
