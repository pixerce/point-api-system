package com.point.system.domain.entity;

import com.point.system.domain.valueobject.LifespanUnit;
import com.point.system.domain.valueobject.PointType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@RequiredArgsConstructor
public class PointLifespanPolicy {

    private final PointType pointType;
    private final Long lifespan;
    private final LifespanUnit unit;
}
