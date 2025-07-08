package com.gongspot.project.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1799627420L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.gongspot.project.common.entity.QBaseEntity _super = new com.gongspot.project.common.entity.QBaseEntity(this);

    public final EnumPath<com.gongspot.project.common.enums.CongestionEnum> congestion = createEnum("congestion", com.gongspot.project.common.enums.CongestionEnum.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> datetime = createDateTime("datetime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final ListPath<com.gongspot.project.common.enums.FacilitiesEnum, EnumPath<com.gongspot.project.common.enums.FacilitiesEnum>> facilities = this.<com.gongspot.project.common.enums.FacilitiesEnum, EnumPath<com.gongspot.project.common.enums.FacilitiesEnum>>createList("facilities", com.gongspot.project.common.enums.FacilitiesEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.gongspot.project.common.enums.MoodEnum, EnumPath<com.gongspot.project.common.enums.MoodEnum>> mood = this.<com.gongspot.project.common.enums.MoodEnum, EnumPath<com.gongspot.project.common.enums.MoodEnum>>createList("mood", com.gongspot.project.common.enums.MoodEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final com.gongspot.project.domain.place.entity.QPlace place;

    public final ListPath<com.gongspot.project.common.enums.PurposeEnum, EnumPath<com.gongspot.project.common.enums.PurposeEnum>> purpose = this.<com.gongspot.project.common.enums.PurposeEnum, EnumPath<com.gongspot.project.common.enums.PurposeEnum>>createList("purpose", com.gongspot.project.common.enums.PurposeEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.gongspot.project.domain.user.entity.QUser user;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new com.gongspot.project.domain.place.entity.QPlace(forProperty("place"), inits.get("place")) : null;
        this.user = inits.isInitialized("user") ? new com.gongspot.project.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

