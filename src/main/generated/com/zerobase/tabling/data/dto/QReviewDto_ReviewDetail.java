package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReviewDto_ReviewDetail is a Querydsl Projection type for ReviewDetail
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReviewDto_ReviewDetail extends ConstructorExpression<ReviewDto.ReviewDetail> {

    private static final long serialVersionUID = -1690781864L;

    public QReviewDto_ReviewDetail(com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt, com.querydsl.core.types.Expression<? extends ReservationDto.ForResponse> reservation, com.querydsl.core.types.Expression<? extends AuthDto.ForResponse> user, com.querydsl.core.types.Expression<Integer> rate, com.querydsl.core.types.Expression<String> context) {
        super(ReviewDto.ReviewDetail.class, new Class<?>[]{java.time.LocalDateTime.class, java.time.LocalDateTime.class, ReservationDto.ForResponse.class, AuthDto.ForResponse.class, int.class, String.class}, createdAt, updatedAt, reservation, user, rate, context);
    }

}

