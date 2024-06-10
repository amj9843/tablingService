package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReservationDto_ForUser is a Querydsl Projection type for ForUser
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReservationDto_ForUser extends ConstructorExpression<ReservationDto.ForUser> {

    private static final long serialVersionUID = 965927393L;

    public QReservationDto_ForUser(com.querydsl.core.types.Expression<Long> reservationId, com.querydsl.core.types.Expression<? extends StoreDto.ForResponse> store, com.querydsl.core.types.Expression<Integer> headCount, com.querydsl.core.types.Expression<com.zerobase.tabling.data.constant.ReservationStatus> status) {
        super(ReservationDto.ForUser.class, new Class<?>[]{long.class, StoreDto.ForResponse.class, int.class, com.zerobase.tabling.data.constant.ReservationStatus.class}, reservationId, store, headCount, status);
    }

}

