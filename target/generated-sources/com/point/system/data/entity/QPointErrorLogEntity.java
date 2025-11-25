package com.point.system.data.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPointErrorLogEntity is a Querydsl query type for PointErrorLogEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointErrorLogEntity extends EntityPathBase<PointErrorLogEntity> {

    private static final long serialVersionUID = 250084500L;

    public static final QPointErrorLogEntity pointErrorLogEntity = new QPointErrorLogEntity("pointErrorLogEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> errorId = createNumber("errorId", Long.class);

    public final StringPath failureReason = createString("failureReason");

    public final StringPath referenceId = createString("referenceId");

    public final EnumPath<com.point.system.domain.valueobject.StatusType> status = createEnum("status", com.point.system.domain.valueobject.StatusType.class);

    public final EnumPath<com.point.system.domain.valueobject.TransactionType> transactionType = createEnum("transactionType", com.point.system.domain.valueobject.TransactionType.class);

    public QPointErrorLogEntity(String variable) {
        super(PointErrorLogEntity.class, forVariable(variable));
    }

    public QPointErrorLogEntity(Path<? extends PointErrorLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPointErrorLogEntity(PathMetadata metadata) {
        super(PointErrorLogEntity.class, metadata);
    }

}

