package com.gongspot.project.domain.place.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlace is a Querydsl query type for Place
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlace extends EntityPathBase<Place> {

    private static final long serialVersionUID = -1919072262L;

    public static final QPlace place = new QPlace("place");

    public final com.gongspot.project.common.entity.QBaseEntity _super = new com.gongspot.project.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final ListPath<com.gongspot.project.common.enums.FacilitiesEnum, EnumPath<com.gongspot.project.common.enums.FacilitiesEnum>> facilities = this.<com.gongspot.project.common.enums.FacilitiesEnum, EnumPath<com.gongspot.project.common.enums.FacilitiesEnum>>createList("facilities", com.gongspot.project.common.enums.FacilitiesEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final ListPath<com.gongspot.project.domain.home.entity.HotCheck, com.gongspot.project.domain.home.entity.QHotCheck> hotCheckList = this.<com.gongspot.project.domain.home.entity.HotCheck, com.gongspot.project.domain.home.entity.QHotCheck>createList("hotCheckList", com.gongspot.project.domain.home.entity.HotCheck.class, com.gongspot.project.domain.home.entity.QHotCheck.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath information = createString("information");

    public final BooleanPath isFree = createBoolean("isFree");

    public final ListPath<com.gongspot.project.common.enums.LocationEnum, EnumPath<com.gongspot.project.common.enums.LocationEnum>> location = this.<com.gongspot.project.common.enums.LocationEnum, EnumPath<com.gongspot.project.common.enums.LocationEnum>>createList("location", com.gongspot.project.common.enums.LocationEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final ListPath<com.gongspot.project.common.enums.MoodEnum, EnumPath<com.gongspot.project.common.enums.MoodEnum>> mood = this.<com.gongspot.project.common.enums.MoodEnum, EnumPath<com.gongspot.project.common.enums.MoodEnum>>createList("mood", com.gongspot.project.common.enums.MoodEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final ListPath<com.gongspot.project.common.enums.PurposeEnum, EnumPath<com.gongspot.project.common.enums.PurposeEnum>> purpose = this.<com.gongspot.project.common.enums.PurposeEnum, EnumPath<com.gongspot.project.common.enums.PurposeEnum>>createList("purpose", com.gongspot.project.common.enums.PurposeEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final ListPath<com.gongspot.project.common.enums.PlaceEnum, EnumPath<com.gongspot.project.common.enums.PlaceEnum>> type = this.<com.gongspot.project.common.enums.PlaceEnum, EnumPath<com.gongspot.project.common.enums.PlaceEnum>>createList("type", com.gongspot.project.common.enums.PlaceEnum.class, EnumPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlace(String variable) {
        super(Place.class, forVariable(variable));
    }

    public QPlace(Path<? extends Place> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlace(PathMetadata metadata) {
        super(Place.class, metadata);
    }

}

