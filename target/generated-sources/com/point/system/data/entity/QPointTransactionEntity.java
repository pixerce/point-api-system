package com.point.system.data.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPointTransactionEntity is a Querydsl query type for PointTransactionEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointTransactionEntity extends EntityPathBase<PointTransactionEntity> {

    private static final long serialVersionUID = 1249746636L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPointTransactionEntity pointTransactionEntity = new QPointTransactionEntity("pointTransactionEntity");

    public final com.point.system.common.entity.QBaseEntity _super = new com.point.system.common.entity.QBaseEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath failureReason = createString("failureReason");

    public final StringPath referenceId = createString("referenceId");

    public final EnumPath<com.point.system.domain.valueobject.StatusType> status = createEnum("status", com.point.system.domain.valueobject.StatusType.class);

    public final NumberPath<Long> transactionId = createNumber("transactionId", Long.class);

    public final EnumPath<com.point.system.domain.valueobject.TransactionType> transactionType = createEnum("transactionType", com.point.system.domain.valueobject.TransactionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUserPointEntity userPoint;

    public QPointTransactionEntity(String variable) {
        this(PointTransactionEntity.class, forVariable(variable), INITS);
    }

    public QPointTransactionEntity(Path<? extends PointTransactionEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPointTransactionEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPointTransactionEntity(PathMetadata metadata, PathInits inits) {
        this(PointTransactionEntity.class, metadata, inits);
    }

    public QPointTransactionEntity(Class<? extends PointTransactionEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userPoint = inits.isInitialized("userPoint") ? new QUserPointEntity(forProperty("userPoint")) : null;
    }

}

