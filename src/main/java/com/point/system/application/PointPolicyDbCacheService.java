package com.point.system.application;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointPolicyDbCacheService {

    private final PointTypePolicyJpaRepository pointTypePolicyJpaRepository;
    private final PointLifespanPolicyJpaRepository pointLifespanPolicyJpaRepository;
    private final PointPolicyDbMapper pointPolicyDbMapper;

    @CachePut(value = "pointPolicies", key = "'policy_type_' + #pointType")
    public List<PointTypePolicy> getPointTypePolicyList(PointType pointType) {
        List<PointTypePolicyEntity> pointTypePolicyEntityList = this.pointTypePolicyJpaRepository.findByIsActiveEqualsAndPointTypeEquals(IsActiveYn.Y, pointType);

        List<PointTypePolicy> pointTypePolicyList = pointTypePolicyEntityList.stream()
                .map(pointPolicyDbMapper::pointTypePolicyEntityToPointTypePolicy)
                .toList();

        log.info("Caching pointPolicies: {}", pointTypePolicyList.size());
        return pointTypePolicyList;
    }

    @CachePut(value = "pointPolicies", key = "'policy_lifespan_' + #pointType")
    public List<PointLifespanPolicy> getPointLifespanPolicyList(PointType pointType) {
        List<PointLifespanPolicyEntity> pointLifespanPolicyEntityList = this.pointLifespanPolicyJpaRepository.findByIsActiveEqualsAndPointTypeEquals(IsActiveYn.Y, pointType);
        List<PointLifespanPolicy> pointLifespanPolicyList = pointLifespanPolicyEntityList.stream()
                .map(this.pointPolicyDbMapper::pointLifespanPolicyEntityToPointLifespanPolicy)
                .toList();

        log.info("Caching pointPolicies: {}", pointLifespanPolicyList.size());
        return pointLifespanPolicyList;
    }
}
