package com.gongspot.project.domain.newplace.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNewPlace is a Querydsl query type for NewPlace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNewPlace extends EntityPathBase<NewPlace> {

    private static final long serialVersionUID = -1617266014L;

    public static final QNewPlace newPlace = new QNewPlace("newPlace");

    public final com.gongspot.project.common.entity.QBaseEntity _super = new com.gongspot.project.common.entity.QBaseEntity(this);

    public final BooleanPath approve = createBoolean("approve");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath deleted = createBoolean("deleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath link = createString("link");

    public final StringPath name = createString("name");

    public final StringPath reason = createString("reason");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QNewPlace(String variable) {
        super(NewPlace.class, forVariable(variable));
    }

    public QNewPlace(Path<? extends NewPlace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNewPlace(PathMetadata metadata) {
        super(NewPlace.class, metadata);
    }

}

