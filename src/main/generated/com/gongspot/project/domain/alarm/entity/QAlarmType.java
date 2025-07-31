package com.gongspot.project.domain.alarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlarmType is a Querydsl query type for AlarmType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmType extends EntityPathBase<AlarmType> {

    private static final long serialVersionUID = 659064468L;

    public static final QAlarmType alarmType = new QAlarmType("alarmType");

    public final com.gongspot.project.common.entity.QBaseEntity _super = new com.gongspot.project.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.gongspot.project.common.enums.AlarmTypeEnum> type = createEnum("type", com.gongspot.project.common.enums.AlarmTypeEnum.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAlarmType(String variable) {
        super(AlarmType.class, forVariable(variable));
    }

    public QAlarmType(Path<? extends AlarmType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlarmType(PathMetadata metadata) {
        super(AlarmType.class, metadata);
    }

}

