package com.point.system.data.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPointLifespanPolicyEntity is a Querydsl query type for PointLifespanPolicyEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointLifespanPolicyEntity extends EntityPathBase<PointLifespanPolicyEntity> {

    private static final long serialVersionUID = -2026474288L;

    public static final QPointLifespanPolicyEntity pointLifespanPolicyEntity = new QPointLifespanPolicyEntity("pointLifespanPolicyEntity");

    public final com.point.system.common.entity.QBaseEntity _super = new com.point.system.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.point.system.domain.valueobject.IsActiveYn> isActive = createEnum("isActive", com.point.system.domain.valueobject.IsActiveYn.class);

    public final NumberPath<Long> lifespan = createNumber("lifespan", Long.class);

    public final NumberPath<Long> lifespanId = createNumber("lifespanId", Long.class);

    public final EnumPath<com.point.system.domain.valueobject.PointType> pointType = createEnum("pointType", com.point.system.domain.valueobject.PointType.class);

    public final EnumPath<com.point.system.domain.valueobject.LifespanUnit> unit = createEnum("unit", com.point.system.domain.valueobject.LifespanUnit.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPointLifespanPolicyEntity(String variable) {
        super(PointLifespanPolicyEntity.class, forVariable(variable));
    }

    public QPointLifespanPolicyEntity(Path<? extends PointLifespanPolicyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPointLifespanPolicyEntity(PathMetadata metadata) {
        super(PointLifespanPolicyEntity.class, metadata);
    }

}

