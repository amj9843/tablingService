package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReservationDto_ReservationInfo is a Querydsl Projection type for ReservationInfo
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReservationDto_ReservationInfo extends ConstructorExpression<ReservationDto.ReservationInfo> {

    private static final long serialVersionUID = 503700295L;

    public QReservationDto_ReservationInfo(com.querydsl.core.types.Expression<Long> reservationId, com.querydsl.core.types.Expression<java.time.LocalDateTime> time, com.querydsl.core.types.Expression<Integer> headCount, com.querydsl.core.types.Expression<com.zerobase.tabling.data.constant.ReservationStatus> status) {
        super(ReservationDto.ReservationInfo.class, new Class<?>[]{long.class, java.time.LocalDateTime.class, int.class, com.zerobase.tabling.data.constant.ReservationStatus.class}, reservationId, time, headCount, status);
    }

}

