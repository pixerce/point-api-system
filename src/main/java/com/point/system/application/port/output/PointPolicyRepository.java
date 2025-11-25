package com.point.system.application.port.output;

import com.point.system.domain.entity.PointLifespanPolicy;
import com.point.system.domain.entity.PointPolicy;
import com.point.system.domain.entity.PointTypePolicy;
import com.point.system.domain.valueobject.PointType;

import java.util.List;

public interface PointPolicyRepository {

    List<PointTypePolicy> findTypePolicyByPointType(final PointType pointType);

    List<PointLifespanPolicy> findLifespanPolicyByPointType(PointType pointType);
}
