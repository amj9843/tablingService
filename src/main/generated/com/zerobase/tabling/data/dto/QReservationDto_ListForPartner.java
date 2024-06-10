package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReservationDto_ListForPartner is a Querydsl Projection type for ListForPartner
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReservationDto_ListForPartner extends ConstructorExpression<ReservationDto.ListForPartner> {

    private static final long serialVersionUID = 1153643696L;

    public QReservationDto_ListForPartner(com.querydsl.core.types.Expression<java.time.LocalDateTime> reservationTime, com.querydsl.core.types.Expression<? extends java.util.Set<ReservationDto.ForPartner>> reservationInfo) {
        super(ReservationDto.ListForPartner.class, new Class<?>[]{java.time.LocalDateTime.class, java.util.Set.class}, reservationTime, reservationInfo);
    }

}

