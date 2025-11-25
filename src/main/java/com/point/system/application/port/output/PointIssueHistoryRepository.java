package com.point.system.application.port.output;

import com.point.system.domain.entity.PointIssueHistory;

import java.util.Optional;

public interface PointIssueHistoryRepository {

    PointIssueHistory save(PointIssueHistory pointIssueHistory);

    Optional<PointIssueHistory> findByRequestId(String requestId);
}
