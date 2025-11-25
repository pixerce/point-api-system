package com.point.system.data.mapper;

import com.point.system.data.entity.PointTransactionEntity;
import com.point.system.data.entity.UserPointEntity;
import com.point.system.domain.entity.PointTransaction;
import com.point.system.domain.entity.UserPoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PointDbMapper {

    public UserPoint userPointEntityToUserPointWithoutPointTransaction(UserPointEntity userPointEntity) {

        return UserPoint.builder()
                .pointType(userPointEntity.getPointType())
                .userPointId(userPointEntity.getUserPointId())
                .amount(userPointEntity.getAmount())
                .balance(userPointEntity.getBalance())
                .userId(userPointEntity.getUserId())
                .startDate(userPointEntity.getStartDate())
                .expireDate(userPointEntity.getExpireDate())
                .issueMethod(userPointEntity.getIssueMethod())
                .build();
    }

    public UserPoint userPointEntityToUserPoint(UserPointEntity userPointEntity) {

        List<PointTransaction> pointTransactionList = userPointEntity.getPointTransactionList().stream()
                        .map(this::pointTransactionEntityToPointTransaction)
                        .collect(Collectors.toList());

        return UserPoint.builder()
                .pointType(userPointEntity.getPointType())
                .userPointId(userPointEntity.getUserPointId())
                .amount(userPointEntity.getAmount())
                .balance(userPointEntity.getBalance())
                .userId(userPointEntity.getUserId())
                .startDate(userPointEntity.getStartDate())
                .expireDate(userPointEntity.getExpireDate())
                .issueMethod(userPointEntity.getIssueMethod())
                .pointTransactionList(pointTransactionList)
                .build();
    }

    public UserPointEntity userPointToUserPointEntity(UserPoint userPoint) {

        UserPointEntity userPointEntity = UserPointEntity.builder()
                .balance(userPoint.getBalance())
                .userId(userPoint.getUserId())
                .pointType(userPoint.getPointType())
                .startDate(userPoint.getStartDate())
                .expireDate(userPoint.getExpireDate())
                .amount(userPoint.getAmount())
                .issueMethod(userPoint.getIssueMethod())
                .build();

        userPoint.getPointTransactionList().stream()
                .map(this::pointTransactionToPointTransactionEntity)
                .forEach(pt -> userPointEntity.addPointTransaction(pt));

        return userPointEntity;
    }

    public PointTransactionEntity pointTransactionToPointTransactionEntity(PointTransaction pointTransaction) {
        return PointTransactionEntity.builder()
                .transactionType(pointTransaction.getTransactionType())
                .amount(pointTransaction.getAmount())
                .referenceId(pointTransaction.getReferenceId())
                .status(pointTransaction.getStatus())
                .failureReason(pointTransaction.getFailureReason())
                .build();
    }

    public PointTransaction pointTransactionEntityToPointTransaction(PointTransactionEntity entity) {
        return PointTransaction.builder()
                .transactionType(entity.getTransactionType())
                .userPointId(entity.getUserPoint().getUserPointId())
                .transactionId(entity.getTransactionId())
                .createdAt(entity.getCreatedAt())
                .referenceId(entity.getReferenceId())
                .amount(entity.getAmount())
                .failureReason(entity.getFailureReason())
                .status(entity.getStatus())
                .build();

    }

}
