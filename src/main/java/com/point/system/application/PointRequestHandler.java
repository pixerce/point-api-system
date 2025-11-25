package com.point.system.application;

import com.point.system.application.dto.*;
import com.point.system.application.mapper.PointDataMapper;
import com.point.system.application.port.output.PointErrorLogRepository;
import com.point.system.application.port.output.PointIssueHistoryRepository;
import com.point.system.domain.entity.PointIssueHistory;
import com.point.system.domain.entity.UserPoint;
import com.point.system.domain.exception.DomainException;
import com.point.system.domain.valueobject.StatusType;
import com.point.system.domain.valueobject.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointRequestHandler {

    private final PointApplicationDomainService pointApplicationDomainService;
    private final PointIssueHistoryRepository pointIssueHistoryRepository;
    private final PointDataMapper pointDataMapper;
    private final PointErrorLogRepository pointErrorLogRepository;

    public PointIssueResponse issuePoint(PointIssueCommand pointIssueCommand) {
        Optional<PointIssueHistory> optionalPointIssueHistory = this.pointIssueHistoryRepository.findByRequestId(pointIssueCommand.getRequestId());
        if (optionalPointIssueHistory.isPresent())
            return new PointIssueResponse(pointIssueCommand.getRequestId(), optionalPointIssueHistory.get().getUserPointId(), "포인트 적립 성공");

        PointIssueHistory pointIssueHistory = this.pointDataMapper.createPointIssueHistory(pointIssueCommand);
        try {
            UserPoint userPoint = this.pointApplicationDomainService.issuePoint(pointIssueCommand);
            pointIssueHistory.setUserPointId(userPoint.getUserPointId());
            pointIssueHistoryRepository.save(pointIssueHistory);
            return new PointIssueResponse(pointIssueCommand.getRequestId(), userPoint.getUserPointId(), "포인트 적립 성공");
        } catch (Exception e) {
            e.printStackTrace();
            pointIssueHistory.updateFailure(e.getMessage());
            pointIssueHistoryRepository.save(pointIssueHistory);
            this.pointErrorLogRepository.log(pointIssueCommand.getRequestId(), StatusType.FAILED, TransactionType.ISSUE, e.getMessage());
            throw e;
        }
    }

    public void retrievePoint(PointRetrieveCommand pointRetrieveCommand) {
        try {
            this.pointApplicationDomainService.retrievePoint(pointRetrieveCommand);
        } catch (Exception e) {
            this.pointErrorLogRepository.log(pointRetrieveCommand.getRequestId(), StatusType.FAILED, TransactionType.RETRIEVE, e.getMessage());
            throw e;
        }
    }

    public void placeOrder(OrderCommand orderCommand) {
        try {
            this.pointApplicationDomainService.placeOrder(orderCommand);
        } catch (Exception e) {
            this.pointErrorLogRepository.log(orderCommand.getOrderId(), StatusType.FAILED, TransactionType.BUY, e.getMessage());
            throw e;
        }
    }

    public void cancelOrder(CancelCommand cancelCommand) {
        try {
            this.pointApplicationDomainService.cancelOrder(cancelCommand);
        } catch (Exception e) {
            this.pointErrorLogRepository.log(cancelCommand.getOrderId(), StatusType.FAILED, TransactionType.CANCEL, e.getMessage());
            throw e;
        }
    }
}
