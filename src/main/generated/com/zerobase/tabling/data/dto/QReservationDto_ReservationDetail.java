package com.zerobase.tabling.data.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.zerobase.tabling.data.dto.QReservationDto_ReservationDetail is a Querydsl Projection type for ReservationDetail
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReservationDto_ReservationDetail extends ConstructorExpression<ReservationDto.ReservationDetail> {

    private static final long serialVersionUID = -1426371414L;

    public QReservationDto_ReservationDetail(com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<? extends StoreDto.ForResponse> store, com.querydsl.core.types.Expression<? extends ReservationDto.ReservationInfo> reservation) {
        super(ReservationDto.ReservationDetail.class, new Class<?>[]{java.time.LocalDateTime.class, StoreDto.ForResponse.class, ReservationDto.ReservationInfo.class}, createdAt, store, reservation);
    }

}

