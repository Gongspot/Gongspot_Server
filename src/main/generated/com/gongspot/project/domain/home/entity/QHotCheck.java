package com.gongspot.project.domain.home.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHotCheck is a Querydsl query type for HotCheck
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHotCheck extends EntityPathBase<HotCheck> {

    private static final long serialVersionUID = 1720169742L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHotCheck hotCheck = new QHotCheck("hotCheck");

    public final com.gongspot.project.common.entity.QBaseEntity _super = new com.gongspot.project.common.entity.QBaseEntity(this);

    public final NumberPath<Long> cnt = createNumber("cnt", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.gongspot.project.domain.place.entity.QPlace place;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> week = createNumber("week", Integer.class);

    public QHotCheck(String variable) {
        this(HotCheck.class, forVariable(variable), INITS);
    }

    public QHotCheck(Path<? extends HotCheck> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHotCheck(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHotCheck(PathMetadata metadata, PathInits inits) {
        this(HotCheck.class, metadata, inits);
    }

    public QHotCheck(Class<? extends HotCheck> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new com.gongspot.project.domain.place.entity.QPlace(forProperty("place")) : null;
    }

}

