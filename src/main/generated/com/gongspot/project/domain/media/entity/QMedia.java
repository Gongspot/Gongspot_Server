package com.gongspot.project.domain.media.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMedia is a Querydsl query type for Media
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMedia extends EntityPathBase<Media> {

    private static final long serialVersionUID = 1131943706L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMedia media = new QMedia("media");

    public final com.gongspot.project.common.entity.QBaseEntity _super = new com.gongspot.project.common.entity.QBaseEntity(this);

    public final com.gongspot.project.domain.banner.entity.QBanner banner;

    public final StringPath contentType = createString("contentType");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isThumbnail = createBoolean("isThumbnail");

    public final com.gongspot.project.domain.notification.entity.QNotification notification;

    public final StringPath originalFileName = createString("originalFileName");

    public final com.gongspot.project.domain.place.entity.QPlace place;

    public final com.gongspot.project.domain.review.entity.QReview review;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public QMedia(String variable) {
        this(Media.class, forVariable(variable), INITS);
    }

    public QMedia(Path<? extends Media> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMedia(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMedia(PathMetadata metadata, PathInits inits) {
        this(Media.class, metadata, inits);
    }

    public QMedia(Class<? extends Media> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.banner = inits.isInitialized("banner") ? new com.gongspot.project.domain.banner.entity.QBanner(forProperty("banner")) : null;
        this.notification = inits.isInitialized("notification") ? new com.gongspot.project.domain.notification.entity.QNotification(forProperty("notification")) : null;
        this.place = inits.isInitialized("place") ? new com.gongspot.project.domain.place.entity.QPlace(forProperty("place")) : null;
        this.review = inits.isInitialized("review") ? new com.gongspot.project.domain.review.entity.QReview(forProperty("review"), inits.get("review")) : null;
    }

}

