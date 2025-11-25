package com.point.system.data.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPointTypePolicyEntity is a Querydsl query type for PointTypePolicyEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointTypePolicyEntity extends EntityPathBase<PointTypePolicyEntity> {

    private static final long serialVersionUID = 1346408516L;

    public static final QPointTypePolicyEntity pointTypePolicyEntity = new QPointTypePolicyEntity("pointTypePolicyEntity");

    public final com.point.system.common.entity.QBaseEntity _super = new com.point.system.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.point.system.domain.valueobject.CumulativePeriod> cumulativePeriod = createEnum("cumulativePeriod", com.point.system.domain.valueobject.CumulativePeriod.class);

    public final EnumPath<com.point.system.domain.valueobject.IsActiveYn> isActive = createEnum("isActive", com.point.system.domain.valueobject.IsActiveYn.class);

    public final NumberPath<Long> maxCumulativeAmount = createNumber("maxCumulativeAmount", Long.class);

    public final NumberPath<Long> minAmount = createNumber("minAmount", Long.class);

    public final EnumPath<com.point.system.domain.valueobject.PointType> pointType = createEnum("pointType", com.point.system.domain.valueobject.PointType.class);

    public final NumberPath<Long> typePolicyId = createNumber("typePolicyId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPointTypePolicyEntity(String variable) {
        super(PointTypePolicyEntity.class, forVariable(variable));
    }

    public QPointTypePolicyEntity(Path<? extends PointTypePolicyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPointTypePolicyEntity(PathMetadata metadata) {
        super(PointTypePolicyEntity.class, metadata);
    }

}

