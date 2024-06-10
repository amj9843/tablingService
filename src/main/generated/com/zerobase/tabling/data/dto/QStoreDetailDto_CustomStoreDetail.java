package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QStoreDetailDto_CustomStoreDetail is a Querydsl Projection type for CustomStoreDetail
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QStoreDetailDto_CustomStoreDetail extends ConstructorExpression<StoreDetailDto.CustomStoreDetail> {

    private static final long serialVersionUID = -1360960312L;

    public QStoreDetailDto_CustomStoreDetail(com.querydsl.core.types.Expression<Long> storeDetailId, com.querydsl.core.types.Expression<Long> storeId, com.querydsl.core.types.Expression<java.time.LocalDateTime> reservationTime, com.querydsl.core.types.Expression<Integer> headCount) {
        super(StoreDetailDto.CustomStoreDetail.class, new Class<?>[]{long.class, long.class, java.time.LocalDateTime.class, int.class}, storeDetailId, storeId, reservationTime, headCount);
    }

}

