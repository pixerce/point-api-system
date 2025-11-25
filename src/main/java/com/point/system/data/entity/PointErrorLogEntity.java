package com.point.system.data.entity;

import com.point.system.domain.valueobject.StatusType;
import com.point.system.domain.valueobject.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "point_error_log", schema = "point")
public class PointErrorLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long errorId;
    private String referenceId;
    @Enumerated(EnumType.STRING)
    private StatusType status;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String failureReason;
    @CreatedDate
    private LocalDateTime createdAt;
}
