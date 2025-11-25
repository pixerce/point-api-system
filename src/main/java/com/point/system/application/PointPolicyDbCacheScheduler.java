package com.point.system.application;

import com.point.system.domain.valueobject.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PointPolicyDbCacheScheduler {

    private final PointPolicyDbCacheService pointPolicyDbCacheService;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void refreshPointPolicyCache() {

        for (PointType pointType : PointType.values()) {
            pointPolicyDbCacheService.getPointTypePolicyList(pointType);
            pointPolicyDbCacheService.getPointLifespanPolicyList(pointType);
        }
    }
}
