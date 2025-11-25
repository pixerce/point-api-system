package com.point.system.data.mapper;

import com.point.system.data.entity.PointIssueHistoryEntity;
import com.point.system.domain.entity.PointIssueHistory;
import com.point.system.domain.valueobject.StatusType;
import org.springframework.stereotype.Component;

@Component
public class PointIssueHistoryDbMapper {

    public PointIssueHistoryEntity pointIssueHistoryToPointIssueHistoryEntity(PointIssueHistory pointIssueHistory) {
        return PointIssueHistoryEntity.builder()
                .requestId(pointIssueHistory.getRequestId())
                .userPointId(pointIssueHistory.getUserPointId())
                .pointType(pointIssueHistory.getPointType())
                .issueBy(pointIssueHistory.getIssueBy())
                .userId(pointIssueHistory.getUserId())
                .amount(pointIssueHistory.getAmount())
                .status(pointIssueHistory.getStatus())
                .issueReason(pointIssueHistory.getIssueReason())
                .failureReason(pointIssueHistory.getFailureReason())
                .build();
    }

    public PointIssueHistory pointIssueHistoryEntityToPointIssueHistory(PointIssueHistoryEntity pointIssueHistoryEntity) {
        return PointIssueHistory.builder()
                .issueReason(pointIssueHistoryEntity.getIssueReason())
                .userPointId(pointIssueHistoryEntity.getUserPointId())
                .pointType(pointIssueHistoryEntity.getPointType())
                .createdAt(pointIssueHistoryEntity.getCreatedAt())
                .requestId(pointIssueHistoryEntity.getRequestId())
                .userId(pointIssueHistoryEntity.getUserId())
                .amount(pointIssueHistoryEntity.getAmount())
                .status(pointIssueHistoryEntity.getStatus())
                .failureReason(pointIssueHistoryEntity.getFailureReason())
                .createdAt(pointIssueHistoryEntity.getCreatedAt())
                .issueBy(pointIssueHistoryEntity.getIssueBy())
                .build();
    }
}
