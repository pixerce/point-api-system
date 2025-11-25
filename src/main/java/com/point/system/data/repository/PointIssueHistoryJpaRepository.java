package com.point.system.data.repository;

import com.point.system.data.entity.PointIssueHistoryEntity;
import com.point.system.domain.entity.PointIssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointIssueHistoryJpaRepository extends JpaRepository<PointIssueHistoryEntity, Long> {

    Optional<PointIssueHistoryEntity> findByRequestId(String referenceId);

}
