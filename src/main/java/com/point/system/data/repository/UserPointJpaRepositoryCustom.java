package com.point.system.data.repository;

import com.point.system.data.entity.UserPointEntity;
import com.point.system.domain.valueobject.PointType;
import com.point.system.domain.valueobject.UserPointBalance;

import java.util.List;

public interface UserPointJpaRepositoryCustom {

    UserPointBalance getTotalBalance(String userId, PointType pointType);

    List<UserPointEntity> findUserPointList(String orderId);
}
