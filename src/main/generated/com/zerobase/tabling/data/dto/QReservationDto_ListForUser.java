package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReservationDto_ListForUser is a Querydsl Projection type for ListForUser
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReservationDto_ListForUser extends ConstructorExpression<ReservationDto.ListForUser> {

    private static final long serialVersionUID = 1082776803L;

    public QReservationDto_ListForUser(com.querydsl.core.types.Expression<java.time.LocalDateTime> reservationTime, com.querydsl.core.types.Expression<? extends java.util.Set<ReservationDto.ForUser>> reservationInfo) {
        super(ReservationDto.ListForUser.class, new Class<?>[]{java.time.LocalDateTime.class, java.util.Set.class}, reservationTime, reservationInfo);
    }

}

