package com.point.system.data.adapter;

import com.point.system.application.port.output.PointPolicyRepository;
import com.point.system.data.entity.PointLifespanPolicyEntity;
import com.point.system.data.entity.PointTypePolicyEntity;
import com.point.system.data.mapper.PointPolicyDbMapper;
import com.point.system.data.repository.PointLifespanPolicyJpaRepository;
import com.point.system.data.repository.PointTypePolicyJpaRepository;
import com.point.system.domain.entity.PointLifespanPolicy;
import com.point.system.domain.entity.PointTypePolicy;
import com.point.system.domain.valueobject.IsActiveYn;
import com.point.system.domain.valueobject.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PointPolicyRepositoryImpl implements PointPolicyRepository {

    private final PointPolicyDbMapper pointPolicyPersistMapper;
    private final PointTypePolicyJpaRepository pointTypePolicyJpaRepository;
    private final PointLifespanPolicyJpaRepository pointLifespanPolicyJpaRepository;

    @Override
    @Cacheable(value = "pointPolicies", key = "'policy_type_' + #pointType")
    public List<PointTypePolicy> findTypePolicyByPointType(PointType pointType) {
        List< PointTypePolicyEntity> pointTypePolicyEntityList = this.pointTypePolicyJpaRepository.findByIsActiveEqualsAndPointTypeEquals(IsActiveYn.Y, pointType);
        return pointTypePolicyEntityList.stream()
                .map(this.pointPolicyPersistMapper::pointTypePolicyEntityToPointTypePolicy)
                .toList();
    }

    @Override
    @Cacheable(value = "pointPolicies", key = "'policy_lifespan_' + #pointType")
    public List<PointLifespanPolicy> findLifespanPolicyByPointType(PointType pointType) {
        List<PointLifespanPolicyEntity> pointTypePolicyEntityList = this.pointLifespanPolicyJpaRepository.findByIsActiveEqualsAndPointTypeEquals(IsActiveYn.Y, pointType);
        return pointTypePolicyEntityList.stream()
                .map(this.pointPolicyPersistMapper::pointLifespanPolicyEntityToPointLifespanPolicy)
                .toList();
    }
}
