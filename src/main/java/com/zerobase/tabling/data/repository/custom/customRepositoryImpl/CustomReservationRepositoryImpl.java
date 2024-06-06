package com.zerobase.tabling.data.repository.custom.customRepositoryImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.QReservation;
import com.zerobase.tabling.data.domain.QStoreDetail;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class CustomReservationRepositoryImpl implements CustomReservationRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByUserIdAndStoreDetailIdNowReserving(Long userId, Long storeDetailId) {
        QReservation reservation = QReservation.reservation;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(reservation)
                .where(
                        reservation.storeDetailId.eq(storeDetailId).and(reservation.userId.eq(userId)),
                        reservation.status.eq(ReservationStatus.APPLIED)
                                .or(reservation.status.eq(ReservationStatus.APPROVED))
                )
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public boolean existsReservationByReservationIdAndTime(Long reservationId, LocalDateTime now) {
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(reservation)
                .join(storeDetail)
                .on(
                        reservation.storeDetailId.eq(storeDetail.storeDetailId)
                )
                .where(
                        reservation.reservationId.eq(reservationId),
                        reservation.status.eq(ReservationStatus.APPROVED),
                        storeDetail.reservationTime.between(now.minusMinutes(30), now.minusMinutes(10))
                )
                .fetchFirst();

        return fetchOne != null;
    }
}
