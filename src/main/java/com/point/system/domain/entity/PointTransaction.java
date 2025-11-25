package com.point.system.domain.entity;

import com.point.system.domain.valueobject.StatusType;
import com.point.system.domain.valueobject.TransactionType;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class PointTransaction {
    @Setter
    private Long transactionId;
    private final Long userPointId;
    private final String referenceId;
    private final Long amount;
    private final TransactionType transactionType;
    private final LocalDateTime createdAt;

    @Setter
    private StatusType status;
    @Setter
    private String failureReason;

    public void updateFailure(String failureReason) {
        this.failureReason = failureReason;
        this.status = StatusType.FAILED;
    }

    public void updateSuccess() {
        this.status = StatusType.SUCCESS;
    }

}
