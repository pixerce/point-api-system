package com.point.system.data.entity;

import com.point.system.common.entity.BaseEntity;
import com.point.system.domain.valueobject.IsActiveYn;
import com.point.system.domain.valueobject.PointType;
import com.point.system.domain.valueobject.CumulativePeriod;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "point_type_policy", schema = "point")
public class PointTypePolicyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typePolicyId;

    @Enumerated(EnumType.STRING)
    private PointType pointType;
    private Long minAmount;
    private Long maxCumulativeAmount;
    @Enumerated(EnumType.STRING)
    private CumulativePeriod cumulativePeriod;
    @Enumerated(EnumType.STRING)
    private IsActiveYn isActive;
}
