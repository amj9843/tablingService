package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReservationDto_ForResponse is a Querydsl Projection type for ForResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReservationDto_ForResponse extends ConstructorExpression<ReservationDto.ForResponse> {

    private static final long serialVersionUID = -1956769961L;

    public QReservationDto_ForResponse(com.querydsl.core.types.Expression<Long> reservationId, com.querydsl.core.types.Expression<java.time.LocalDateTime> time, com.querydsl.core.types.Expression<Integer> headCount) {
        super(ReservationDto.ForResponse.class, new Class<?>[]{long.class, java.time.LocalDateTime.class, int.class}, reservationId, time, headCount);
    }

}

