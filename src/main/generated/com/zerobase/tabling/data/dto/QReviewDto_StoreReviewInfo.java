package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReviewDto_StoreReviewInfo is a Querydsl Projection type for StoreReviewInfo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReviewDto_StoreReviewInfo extends ConstructorExpression<ReviewDto.StoreReviewInfo> {

    private static final long serialVersionUID = 410163192L;

    public QReviewDto_StoreReviewInfo(com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt, com.querydsl.core.types.Expression<Long> reviewId, com.querydsl.core.types.Expression<Integer> rate, com.querydsl.core.types.Expression<String> context) {
        super(ReviewDto.StoreReviewInfo.class, new Class<?>[]{java.time.LocalDateTime.class, java.time.LocalDateTime.class, long.class, int.class, String.class}, createdAt, updatedAt, reviewId, rate, context);
    }

}

