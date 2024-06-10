package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReservationDto_ForPartner is a Querydsl Projection type for ForPartner
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReservationDto_ForPartner extends ConstructorExpression<ReservationDto.ForPartner> {

    private static final long serialVersionUID = -983619854L;

    public QReservationDto_ForPartner(com.querydsl.core.types.Expression<Long> reservationId, com.querydsl.core.types.Expression<? extends AuthDto.ForResponse> user, com.querydsl.core.types.Expression<Integer> headCount, com.querydsl.core.types.Expression<com.zerobase.tabling.data.constant.ReservationStatus> status) {
        super(ReservationDto.ForPartner.class, new Class<?>[]{long.class, AuthDto.ForResponse.class, int.class, com.zerobase.tabling.data.constant.ReservationStatus.class}, reservationId, user, headCount, status);
    }

}

