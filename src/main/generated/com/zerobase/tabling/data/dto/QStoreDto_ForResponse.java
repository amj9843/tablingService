package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QStoreDto_ForResponse is a Querydsl Projection type for ForResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QStoreDto_ForResponse extends ConstructorExpression<StoreDto.ForResponse> {

    private static final long serialVersionUID = -457948254L;

    public QStoreDto_ForResponse(com.querydsl.core.types.Expression<Long> storeId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> location, com.querydsl.core.types.Expression<String> description) {
        super(StoreDto.ForResponse.class, new Class<?>[]{long.class, String.class, String.class, String.class}, storeId, name, location, description);
    }

}

