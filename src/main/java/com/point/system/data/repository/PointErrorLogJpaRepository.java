package com.point.system.data.repository;

import com.point.system.data.entity.PointErrorLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointErrorLogJpaRepository extends JpaRepository<PointErrorLogEntity, Long> {
}
