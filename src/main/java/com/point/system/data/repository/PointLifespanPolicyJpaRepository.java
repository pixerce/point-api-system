package com.point.system.data.repository;

import com.point.system.data.entity.PointLifespanPolicyEntity;
import com.point.system.domain.valueobject.IsActiveYn;
import com.point.system.domain.valueobject.PointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointLifespanPolicyJpaRepository extends JpaRepository<PointLifespanPolicyEntity, Long> {

    List<PointLifespanPolicyEntity> findByIsActiveEqualsAndPointTypeEquals(IsActiveYn isActive, PointType pointType);

}
