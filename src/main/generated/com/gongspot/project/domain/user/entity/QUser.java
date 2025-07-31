package com.gongspot.project.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 574467722L;

    public static final QUser user = new QUser("user");

    public final com.gongspot.project.common.entity.QBaseEntity _super = new com.gongspot.project.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.gongspot.project.common.enums.LocationEnum, EnumPath<com.gongspot.project.common.enums.LocationEnum>> location = this.<com.gongspot.project.common.enums.LocationEnum, EnumPath<com.gongspot.project.common.enums.LocationEnum>>createList("location", com.gongspot.project.common.enums.LocationEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final ListPath<com.gongspot.project.common.enums.PlaceEnum, EnumPath<com.gongspot.project.common.enums.PlaceEnum>> preferPlace = this.<com.gongspot.project.common.enums.PlaceEnum, EnumPath<com.gongspot.project.common.enums.PlaceEnum>>createList("preferPlace", com.gongspot.project.common.enums.PlaceEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath profileImg = createString("profileImg");

    public final ListPath<com.gongspot.project.common.enums.PurposeEnum, EnumPath<com.gongspot.project.common.enums.PurposeEnum>> purpose = this.<com.gongspot.project.common.enums.PurposeEnum, EnumPath<com.gongspot.project.common.enums.PurposeEnum>>createList("purpose", com.gongspot.project.common.enums.PurposeEnum.class, EnumPath.class, PathInits.DIRECT2);

    public final EnumPath<com.gongspot.project.common.enums.RoleEnum> role = createEnum("role", com.gongspot.project.common.enums.RoleEnum.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

