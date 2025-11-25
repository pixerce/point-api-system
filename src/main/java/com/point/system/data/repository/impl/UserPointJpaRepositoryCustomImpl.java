package com.point.system.data.repository.impl;

import com.point.system.data.entity.UserPointEntity;
import com.point.system.data.repository.UserPointJpaRepositoryCustom;
import com.point.system.domain.valueobject.PointType;
import com.point.system.domain.valueobject.UserPointBalance;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.point.system.common.Constants.ASIA_SEOUL;
import static com.point.system.data.entity.QPointTransactionEntity.pointTransactionEntity;
import static com.point.system.data.entity.QUserPointEntity.userPointEntity;

@RequiredArgsConstructor
@Repository
public class UserPointJpaRepositoryCustomImpl implements UserPointJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public UserPointBalance getTotalBalance(String userId, PointType pointType) {

        final LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(ASIA_SEOUL));

        BooleanBuilder booleanBuilder = new BooleanBuilder()
                .and(userPointEntity.userId.eq(userId))
                .and(userPointEntity.startDate.loe(currentTime))
                .and(userPointEntity.expireDate.goe(currentTime));

        if (pointType != PointType.ALL)
            booleanBuilder.and(userPointEntity.pointType.eq(pointType));

        Long sum = queryFactory
                .select(userPointEntity.balance.sum())
                .from(userPointEntity)
                .where(booleanBuilder)
                .fetchOne();
        return new UserPointBalance(sum);
    }

    @Override
    public List<UserPointEntity> findUserPointList(String orderId) {

        return this.queryFactory.selectFrom(userPointEntity)
                .join(userPointEntity.pointTransactionList, pointTransactionEntity).fetchJoin()
                .where(pointTransactionEntity.referenceId.eq(orderId))
                .fetch();
    }

}
