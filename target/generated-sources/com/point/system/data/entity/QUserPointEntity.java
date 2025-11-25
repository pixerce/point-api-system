package com.point.system.data.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPointEntity is a Querydsl query type for UserPointEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPointEntity extends EntityPathBase<UserPointEntity> {

    private static final long serialVersionUID = 945042189L;

    public static final QUserPointEntity userPointEntity = new QUserPointEntity("userPointEntity");

    public final com.point.system.common.entity.QBaseEntity _super = new com.point.system.common.entity.QBaseEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> balance = createNumber("balance", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> expireDate = createDateTime("expireDate", java.time.LocalDateTime.class);

    public final EnumPath<com.point.system.domain.valueobject.IssueMethod> issueMethod = createEnum("issueMethod", com.point.system.domain.valueobject.IssueMethod.class);

    public final ListPath<PointTransactionEntity, QPointTransactionEntity> pointTransactionList = this.<PointTransactionEntity, QPointTransactionEntity>createList("pointTransactionList", PointTransactionEntity.class, QPointTransactionEntity.class, PathInits.DIRECT2);

    public final EnumPath<com.point.system.domain.valueobject.PointType> pointType = createEnum("pointType", com.point.system.domain.valueobject.PointType.class);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> userPointId = createNumber("userPointId", Long.class);

    public QUserPointEntity(String variable) {
        super(UserPointEntity.class, forVariable(variable));
    }

    public QUserPointEntity(Path<? extends UserPointEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserPointEntity(PathMetadata metadata) {
        super(UserPointEntity.class, metadata);
    }

}

