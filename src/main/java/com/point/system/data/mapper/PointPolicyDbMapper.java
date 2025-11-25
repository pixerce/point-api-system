package com.point.system.data.mapper;

import com.point.system.data.entity.PointLifespanPolicyEntity;
import com.point.system.data.entity.PointTypePolicyEntity;
import com.point.system.domain.entity.PointLifespanPolicy;
import com.point.system.domain.entity.PointTypePolicy;
import org.springframework.stereotype.Component;


@Component
public class PointPolicyDbMapper {

    public PointTypePolicy pointTypePolicyEntityToPointTypePolicy(PointTypePolicyEntity pointTypePolicyEntity) {
        return PointTypePolicy.builder()
                .poinType(pointTypePolicyEntity.getPointType())
                .maxCumulativeAmount(pointTypePolicyEntity.getMaxCumulativeAmount())
                .minAmount(pointTypePolicyEntity.getMinAmount())
                .cumulativePeriod(pointTypePolicyEntity.getCumulativePeriod())
                .build();
    }

    public PointLifespanPolicy pointLifespanPolicyEntityToPointLifespanPolicy(PointLifespanPolicyEntity pointLifespanPolicyEntity) {
        return PointLifespanPolicy.builder()
                .pointType(pointLifespanPolicyEntity.getPointType())
                .lifespan(pointLifespanPolicyEntity.getLifespan())
                .unit(pointLifespanPolicyEntity.getUnit())
                .build();
    }

}
