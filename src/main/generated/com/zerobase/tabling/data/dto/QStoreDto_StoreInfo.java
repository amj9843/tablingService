package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QStoreDto_StoreInfo is a Querydsl Projection type for StoreInfo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QStoreDto_StoreInfo extends ConstructorExpression<StoreDto.StoreInfo> {

    private static final long serialVersionUID = 2123763367L;

    public QStoreDto_StoreInfo(com.querydsl.core.types.Expression<Long> storeId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> location, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<Double> rate, com.querydsl.core.types.Expression<Long> reviewCount) {
        super(StoreDto.StoreInfo.class, new Class<?>[]{long.class, String.class, String.class, String.class, double.class, long.class}, storeId, name, location, description, rate, reviewCount);
    }

}

