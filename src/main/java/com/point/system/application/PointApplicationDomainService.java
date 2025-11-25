package com.point.system.application;

import com.point.system.application.dto.CancelCommand;
import com.point.system.application.dto.OrderCommand;
import com.point.system.application.dto.PointIssueCommand;
import com.point.system.application.dto.PointRetrieveCommand;
import com.point.system.application.mapper.PointDataMapper;
import com.point.system.application.port.output.PointIssueHistoryRepository;
import com.point.system.application.port.output.PointPolicyRepository;
import com.point.system.application.port.output.PointRepository;
import com.point.system.domain.PointDomainService;
import com.point.system.domain.entity.PointLifespanPolicy;
import com.point.system.domain.entity.PointTransaction;
import com.point.system.domain.entity.PointTypePolicy;
import com.point.system.domain.entity.UserPoint;
import com.point.system.domain.valueobject.PointType;
import com.point.system.domain.valueobject.StatusType;
import com.point.system.domain.valueobject.TransactionType;
import com.point.system.domain.valueobject.UserPointBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PointApplicationDomainService {

    private final PointDomainService pointDomainService;
    private final PointDataMapper pointDataMapper;
    private final PointRepository pointRepository;
    private final PointPolicyRepository pointPolicyRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserPoint issuePoint(PointIssueCommand pointIssueCommand) {

        UserPoint userPoint = this.pointDataMapper.issuePointCommandToUserPoint(pointIssueCommand);
        UserPointBalance userPointBalance = this.getUserPointBalance(pointIssueCommand.getUserId(), pointIssueCommand.getPointType());
        List<PointTypePolicy> pointTypePolicyList = this.pointPolicyRepository.findTypePolicyByPointType(pointIssueCommand.getPointType());
        List<PointLifespanPolicy> pointLifespanPolicyList = this.pointPolicyRepository.findLifespanPolicyByPointType(pointIssueCommand.getPointType());

        pointDomainService.validateAndInitiateIssuePoint(userPoint, pointTypePolicyList, pointLifespanPolicyList, userPointBalance);
        this.pointRepository.save(userPoint);
        return userPoint;
    }

    public void retrievePoint(PointRetrieveCommand pointRetrieveCommand) {
        UserPoint userPoint = this.pointRepository.getUserPoint(pointRetrieveCommand.getUserPointId());
        PointTransaction pointTransaction = createPointTransaction(userPoint, TransactionType.RETRIEVE);

        try {
            this.pointDomainService.validateAndRetrievePoint(userPoint, pointTransaction);
            this.pointRepository.saveRetrievePoint(userPoint, pointTransaction);
        } catch (Exception e) {
            this.pointDomainService.updateRetrieveFailed(e.getMessage(), pointTransaction);
            this.pointRepository.saveRetrievePoint(userPoint, pointTransaction);
            throw e;
        }
    }

    private UserPointBalance getUserPointBalance(final String userId, PointType pointType) {
        return this.pointRepository.getUserPointBalance(userId, pointType);
    }


    private PointTransaction createPointTransaction(UserPoint userPoint, TransactionType transactionType) {
        return PointTransaction.builder()
                .amount(userPoint.getBalance())
                .transactionType(transactionType)
                .userPointId(userPoint.getUserPointId())
                .build();
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

    public void placeOrder(OrderCommand orderCommand) {

        List<PointTransaction> pointTransactionList = this.pointDomainService.order(
                orderCommand.getAmount()
                , orderCommand.getOrderId()
                , getUserPointBalance(orderCommand.getUserId(), PointType.ALL)
                , this.pointRepository.getAvailablePointList(orderCommand.getUserId(), orderCommand.getAmount())
        );

        this.pointRepository.persistOrder(pointTransactionList);
    }

    @Transactional
    public void cancelOrder (CancelCommand cancelCommand) {
        final String orderId = cancelCommand.getOrderId();
        // 수기 지급 > 만료일 순으로 정렬
        List<UserPoint> expiredUserPointList = new ArrayList<>();
        List<UserPoint> userPointList = this.pointRepository.findUserPointListByOrderId(orderId);
        List<PointTransaction> cancelPointTransactionList = this.pointDomainService.cancelOrder(cancelCommand.getOrderId(), cancelCommand.getAmount(), userPointList, expiredUserPointList);

        this.pointRepository.persistCancel(cancelPointTransactionList);

        if (!expiredUserPointList.isEmpty())
            this.reissuePoint(expiredUserPointList, "만료된 포인트 재발급");
//        this.eventPublisher.publishEvent(new PointIssueEvent(expiredPointTransactionList, PointEventType.ISSUE));
    }

    public void reissuePoint(List<UserPoint> userPointList, final String reason) {

        // 취소 정책을 조회하자
        for (UserPoint userPoint : userPointList) {

            Long amount = userPoint.getPointTransactionList().stream().mapToLong(PointTransaction::getAmount).sum();
            PointIssueCommand pointIssueCommand = PointIssueCommand.builder()
                    .requestId(null)
                    .issueBy("Cancellation-System")
                    .pointType(userPoint.getPointType())
                    .issueMethod(userPoint.getIssueMethod())
                    .amount(amount)
                    .issueReason(reason)
                    .userId(userPoint.getUserId())
                    .build();
            this.issuePoint(pointIssueCommand);
        }
    }
}
