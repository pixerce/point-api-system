package com.point.system.data.entity;

import com.point.system.common.entity.BaseEntity;
import com.point.system.domain.valueobject.IsActiveYn;
import com.point.system.domain.valueobject.LifespanUnit;
import com.point.system.domain.valueobject.PointType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "point_lifespan_policy", schema = "point")
public class PointLifespanPolicyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lifespanId;
    @Enumerated(EnumType.STRING)
    private PointType pointType;
    private Long lifespan;
    @Enumerated(EnumType.STRING)
    private LifespanUnit unit;
    @Enumerated(EnumType.STRING)
    private IsActiveYn isActive;
}
