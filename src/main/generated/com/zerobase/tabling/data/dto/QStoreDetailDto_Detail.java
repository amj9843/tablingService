package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QStoreDetailDto_Detail is a Querydsl Projection type for Detail
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QStoreDetailDto_Detail extends ConstructorExpression<StoreDetailDto.Detail> {

    private static final long serialVersionUID = -150586806L;

    public QStoreDetailDto_Detail(com.querydsl.core.types.Expression<Long> storeDetailId, com.querydsl.core.types.Expression<java.time.LocalDateTime> reservationTime, com.querydsl.core.types.Expression<Integer> totalHeadCount, com.querydsl.core.types.Expression<Integer> nowHeadCount) {
        super(StoreDetailDto.Detail.class, new Class<?>[]{long.class, java.time.LocalDateTime.class, int.class, int.class}, storeDetailId, reservationTime, totalHeadCount, nowHeadCount);
    }

}

