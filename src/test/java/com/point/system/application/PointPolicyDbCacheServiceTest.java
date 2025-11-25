package com.point.system.application;

import com.point.system.application.port.output.PointPolicyRepository;
import com.point.system.data.repository.PointLifespanPolicyJpaRepository;
import com.point.system.data.repository.PointTypePolicyJpaRepository;
import com.point.system.domain.valueobject.IsActiveYn;
import com.point.system.domain.valueobject.PointType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest
class PointPolicyDbCacheServiceTest {

    @Autowired
    private PointPolicyRepository pointPolicyRepository;
    @MockitoSpyBean
    private PointLifespanPolicyJpaRepository pointLifespanPolicyJpaRepository;
    @MockitoSpyBean
    private PointTypePolicyJpaRepository pointTypePolicyJpaRepository;

    @DisplayName("포인트 정책 테이블의 데이터는 캐싱한 데이터를 조회한다")
    @Test
    public void testCacheablePolicy() {
        for (PointType pointType : PointType.values()) {
            this.pointPolicyRepository.findTypePolicyByPointType(pointType);
            this.pointPolicyRepository.findLifespanPolicyByPointType(pointType);

            verify(pointLifespanPolicyJpaRepository, times(2))
                    .findByIsActiveEqualsAndPointTypeEquals(IsActiveYn.Y, PointType.FREE);

            verify(pointTypePolicyJpaRepository, times(2))
                    .findByIsActiveEqualsAndPointTypeEquals(IsActiveYn.Y, PointType.FREE);
        }
    }
}