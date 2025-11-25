package com.point.system.application.port.output;

import com.point.system.domain.entity.PointTransaction;
import com.point.system.domain.entity.UserPoint;
import com.point.system.domain.valueobject.PointType;
import com.point.system.domain.valueobject.UserPointBalance;

import java.util.List;


public interface PointRepository {

    void save(UserPoint userPoint);

    UserPointBalance getUserPointBalance(String userId, PointType pointType);

    UserPoint getUserPoint(final Long userPointId);

    void saveRetrievePoint(UserPoint userPoint, PointTransaction pointTransaction);

    List<UserPoint> getAvailablePointList(String userId, Long amount);

    void persistOrder(List<PointTransaction> pointTransactionList);

    List<UserPoint> findUserPointListByOrderId(String orderId);

    void persistCancel(List<PointTransaction> pointTransactionList);
}
