package com.gongspot.project.domain.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 2108355418L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final com.gongspot.project.common.entity.QBaseEntity _super = new com.gongspot.project.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath merchantUid = createString("merchantUid");

    public final StringPath ordererName = createString("ordererName");

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final EnumPath<PaymentStatus> paymentStatus = createEnum("paymentStatus", PaymentStatus.class);

    public final EnumPath<PayMethod> payMethod = createEnum("payMethod", PayMethod.class);

    public final com.gongspot.project.domain.point.entity.QPoint point;

    public final QProduct product;

    public final NumberPath<java.math.BigDecimal> totalPrice = createNumber("totalPrice", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.gongspot.project.domain.user.entity.QUser user;

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.point = inits.isInitialized("point") ? new com.gongspot.project.domain.point.entity.QPoint(forProperty("point"), inits.get("point")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
        this.user = inits.isInitialized("user") ? new com.gongspot.project.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

