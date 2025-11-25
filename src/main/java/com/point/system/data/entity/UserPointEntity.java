package com.point.system.data.entity;

import com.point.system.common.entity.BaseEntity;
import com.point.system.domain.valueobject.IssueMethod;
import com.point.system.domain.valueobject.PointType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_point", schema = "point")
@Entity
public class UserPointEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPointId;

    private String userId;
    private Long amount;
    private Long balance;
    @Enumerated(EnumType.STRING)
    private PointType pointType;
    @Enumerated(EnumType.STRING)
    private IssueMethod issueMethod;
    private LocalDateTime startDate;
    private LocalDateTime expireDate;

    @OneToMany(mappedBy = "userPoint", cascade = CascadeType.ALL)
    private List<PointTransactionEntity> pointTransactionList = new ArrayList<>();

    public void addPointTransaction(PointTransactionEntity pointTransactionEntity) {
        if (this.pointTransactionList == null)
            this.pointTransactionList = new ArrayList<>();
        this.pointTransactionList.add(pointTransactionEntity);
        pointTransactionEntity.setUserPoint(this);
    }

    @Override
    public String toString() {
        int childCount = this.pointTransactionList == null ? 0 : this.pointTransactionList.size();
        return "UserPointEntity{" +
                "userPointId=" + userPointId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", balance=" + balance +
                ", pointType=" + pointType +
                ", issueMethod=" + issueMethod +
                ", startDate=" + startDate +
                ", expireDate=" + expireDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", pointTransactionList size=" + childCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPointEntity that = (UserPointEntity) o;
        return userPointId.equals(that.userPointId);
    }

    @Override
    public int hashCode() { return Objects.hash(this.userPointId); }
}
