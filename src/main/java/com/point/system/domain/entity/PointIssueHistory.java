package com.point.system.domain.entity;

import com.point.system.domain.valueobject.PointType;
import com.point.system.domain.valueobject.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PointIssueHistory {
    private String requestId;
    private Long userPointId;
    private String userId;
    private PointType pointType;
    private Long amount;
    private StatusType status;
    private String issueReason;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String issueBy;

    public void updateFailure(final String failureReason) {
        this.setStatus(StatusType.FAILED);
        this.setFailureReason(failureReason);
    }
}
