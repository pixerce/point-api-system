package com.point.system.data.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPointIssueHistoryEntity is a Querydsl query type for PointIssueHistoryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointIssueHistoryEntity extends EntityPathBase<PointIssueHistoryEntity> {

    private static final long serialVersionUID = -1067476941L;

    public static final QPointIssueHistoryEntity pointIssueHistoryEntity = new QPointIssueHistoryEntity("pointIssueHistoryEntity");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath failureReason = createString("failureReason");

    public final StringPath issueBy = createString("issueBy");

    public final NumberPath<Long> issueId = createNumber("issueId", Long.class);

    public final StringPath issueReason = createString("issueReason");

    public final EnumPath<com.point.system.domain.valueobject.PointType> pointType = createEnum("pointType", com.point.system.domain.valueobject.PointType.class);

    public final StringPath requestId = createString("requestId");

    public final EnumPath<com.point.system.domain.valueobject.StatusType> status = createEnum("status", com.point.system.domain.valueobject.StatusType.class);

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> userPointId = createNumber("userPointId", Long.class);

    public QPointIssueHistoryEntity(String variable) {
        super(PointIssueHistoryEntity.class, forVariable(variable));
    }

    public QPointIssueHistoryEntity(Path<? extends PointIssueHistoryEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPointIssueHistoryEntity(PathMetadata metadata) {
        super(PointIssueHistoryEntity.class, metadata);
    }

}

