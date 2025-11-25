package com.point.system.data.entity;

import com.point.system.domain.valueobject.PointType;
import com.point.system.domain.valueobject.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table (name = "point_issue_history", schema = "point")
public class PointIssueHistoryEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long issueId;
    private String requestId;
    private Long userPointId;
    private String userId;
    @Enumerated(EnumType.STRING)
    private PointType pointType;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private StatusType status;
    private String issueReason;
    private String failureReason;
    private String issueBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "PointIssueHistoryEntity{" +
                "issueId=" + issueId +
                ", requestId='" + requestId + '\'' +
                ", userPointId=" + userPointId +
                ", userId=" + userId +
                ", pointType=" + pointType +
                ", amount=" + amount +
                ", status=" + status +
                ", issueReason='" + issueReason + '\'' +
                ", failureReason='" + failureReason + '\'' +
                ", createdAt=" + createdAt +
                ", issueBy='" + issueBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointIssueHistoryEntity that = (PointIssueHistoryEntity) o;
        return issueId.equals(that.issueId);
    }

    @Override
    public int hashCode() { return Objects.hash(this.issueId); }
}
