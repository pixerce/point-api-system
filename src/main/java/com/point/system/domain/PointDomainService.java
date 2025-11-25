package com.point.system.domain;

import com.point.system.domain.entity.PointLifespanPolicy;
import com.point.system.domain.entity.PointTransaction;
import com.point.system.domain.entity.PointTypePolicy;
import com.point.system.domain.entity.UserPoint;
import com.point.system.domain.exception.DomainException;
import com.point.system.domain.valueobject.StatusType;
import com.point.system.domain.valueobject.TransactionType;
import com.point.system.domain.valueobject.UserPointBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.point.system.common.Constants.ASIA_SEOUL;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointDomainService {

    private final Clock currentZoneClock;

    public void validateAndRetrievePoint(UserPoint userPoint, PointTransaction pointTransaction) {
        pointTransaction.updateSuccess();
        userPoint.validateToRetrieve();
        userPoint.initializeToRetrieve();
    }

    public void validateAndInitiateIssuePoint(UserPoint userPoint, List<PointTypePolicy> pointTypePolicyList
            , List<PointLifespanPolicy> pointLifespanPolicyList, UserPointBalance userPointBalance) {
        log.info("userPoint: {}, type-policy: {}, lifespan-policy: {}", userPoint, pointTypePolicyList, pointLifespanPolicyList);
        userPoint.initializeToIssuePoint(this.currentZoneClock);
        userPoint.validatePoint(pointTypePolicyList, pointLifespanPolicyList, userPointBalance);
        userPoint.addPointTransaction(this.createPointTransaction(userPoint, TransactionType.ISSUE));
    }

    private PointTransaction createPointTransaction(UserPoint userPoint, TransactionType transactionType) {
        return PointTransaction.builder()
                .amount(userPoint.getBalance())
                .transactionType(transactionType)
                .status(StatusType.SUCCESS)
                .userPointId(userPoint.getUserPointId())
                .build();
    }

    public void updateRetrieveFailed(String failureMessage, PointTransaction pointTransaction) {
        pointTransaction.updateFailure(failureMessage);
    }

    public List<PointTransaction> order(Long orderAmount, String orderId, UserPointBalance userPointBalance, List<UserPoint> userPointList) {
        if (userPointBalance.getTotalBalance() < orderAmount)
            throw new DomainException("잔액 부족");

        List<PointTransaction> pointTransactionList = new ArrayList<>();
        for (UserPoint userPoint : userPointList) {
            Long subtract = Math.min(userPoint.getBalance(), orderAmount);
            userPoint.setBalance(userPoint.getBalance() - subtract);
            orderAmount -= subtract;

            pointTransactionList.add(this.createPointTransaction(userPoint, orderId, subtract, TransactionType.BUY));
            if (orderAmount == 0L)
                break;
        }

        return pointTransactionList;
    }

    private PointTransaction createPointTransaction(UserPoint userPoint, String orderId, Long subtract, TransactionType transactionType) {
        return PointTransaction.builder()
                .userPointId(userPoint.getUserPointId())
                .referenceId(orderId)
                .amount(subtract)
                .status(StatusType.SUCCESS)
                .transactionType(transactionType)
                .build();
    }

    public List<PointTransaction> cancelOrder(String orderId, Long cancelAmount, List<UserPoint> userPointList
            ,  List<UserPoint> expiredUserPointList) {
        // 수기 지급 > 만료일 순으로 정렬
        List<UserPoint> sortedUserPointList = userPointList.stream()
                .sorted(Comparator.comparing(UserPoint::getIssueMethod).thenComparing(UserPoint::getExpireDate))
                .toList();
        List<PointTransaction> pointTransactionList = sortedUserPointList.stream()
                .flatMap(userPoint -> userPoint.getPointTransactionList().stream())
                .toList();

        Long totalBuyAmount = pointTransactionList.stream()
                .filter(p -> p.getTransactionType() == TransactionType.BUY).mapToLong(PointTransaction::getAmount)
                .sum();

        Long totalCancelAmount = pointTransactionList.stream()
                .filter(p -> p.getTransactionType() == TransactionType.CANCEL).mapToLong(PointTransaction::getAmount)
                .sum();

        if (totalBuyAmount - totalCancelAmount < cancelAmount)
            throw new DomainException("취소 가능 금액 오류");

        LocalDateTime now = LocalDateTime.now(ZoneId.of(ASIA_SEOUL));
        List<PointTransaction> cancelPointTransactionList = new ArrayList<>();
        Long cancelAmountRequested = cancelAmount;
        for (UserPoint userPoint : sortedUserPointList) {
            Long buyAmount = userPoint.getPointTransactionList().stream()
                    .filter(t -> t.getTransactionType() == TransactionType.BUY)
                    .mapToLong(PointTransaction::getAmount)
                    .sum();

            var subtract = Math.min(buyAmount, cancelAmountRequested);
            if (subtract + userPoint.getBalance() > userPoint.getAmount())
                throw new DomainException("취소 금액 계산 오류");

            cancelPointTransactionList.add(this.createPointTransaction(userPoint, orderId, subtract, TransactionType.CANCEL));
            if (now.isAfter(userPoint.getExpireDate())) {
                userPoint.setPointTransactionList(List.of(this.createPointTransaction(userPoint, orderId, subtract, TransactionType.ISSUE)));
                expiredUserPointList.add(userPoint);
            }

            cancelAmountRequested -= subtract;
            if (cancelAmountRequested == 0L)
                break;
        }

        return cancelPointTransactionList;
//        this.eventPublisher.publishEvent(new PointIssueEvent(expiredPointTransactionList, PointEventType.ISSUE));
    }
}
