package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReviewDto_UserReviewInfo is a Querydsl Projection type for UserReviewInfo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReviewDto_UserReviewInfo extends ConstructorExpression<ReviewDto.UserReviewInfo> {

    private static final long serialVersionUID = -1865834464L;

    public QReviewDto_UserReviewInfo(com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt, com.querydsl.core.types.Expression<Long> storeId, com.querydsl.core.types.Expression<Long> reviewId, com.querydsl.core.types.Expression<Integer> rate, com.querydsl.core.types.Expression<String> context) {
        super(ReviewDto.UserReviewInfo.class, new Class<?>[]{java.time.LocalDateTime.class, java.time.LocalDateTime.class, long.class, long.class, int.class, String.class}, createdAt, updatedAt, storeId, reviewId, rate, context);
    }

}

