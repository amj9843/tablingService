package com.zerobase.tabling.data.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStoreDetail is a Querydsl query type for StoreDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreDetail extends EntityPathBase<StoreDetail> {

    private static final long serialVersionUID = 1906792577L;

    public static final QStoreDetail storeDetail = new QStoreDetail("storeDetail");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> headCount = createNumber("headCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> reservationTime = createDateTime("reservationTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> storeDetailId = createNumber("storeDetailId", Long.class);

    public final NumberPath<Long> storeId = createNumber("storeId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStoreDetail(String variable) {
        super(StoreDetail.class, forVariable(variable));
    }

    public QStoreDetail(Path<? extends StoreDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStoreDetail(PathMetadata metadata) {
        super(StoreDetail.class, metadata);
    }

}

