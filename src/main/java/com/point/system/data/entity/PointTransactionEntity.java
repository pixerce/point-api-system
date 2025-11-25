package com.point.system.data.entity;

import com.point.system.common.entity.BaseEntity;
import com.point.system.domain.valueobject.StatusType;
import com.point.system.domain.valueobject.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point_transaction", schema = "point")
@Entity
public class PointTransactionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private String referenceId;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String failureReason;
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_point_id", nullable = false)
    private UserPointEntity userPoint;

    public void setUserPoint(UserPointEntity userPoint) {
        this.userPoint = userPoint;
    }

    @Override
    public String toString() {
        return "PointTransactionEntity{" +
                "transactionId=" + transactionId +
                ", referenceId=" + referenceId +
                ", amount=" + amount +
                ", transactionType=" + transactionType +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", failureReason=" + failureReason +
                ", status=" + status +
                ", userPointId=" + userPoint.getUserPointId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointTransactionEntity that = (PointTransactionEntity) o;
        return transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() { return Objects.hash(this.transactionId); }
}
