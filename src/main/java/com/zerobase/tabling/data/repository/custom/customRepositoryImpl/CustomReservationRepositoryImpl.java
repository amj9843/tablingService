package com.zerobase.tabling.data.repository.custom.customRepositoryImpl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.QReservation;
import com.zerobase.tabling.data.domain.QStore;
import com.zerobase.tabling.data.domain.QStoreDetail;
import com.zerobase.tabling.data.domain.QUser;
import com.zerobase.tabling.data.dto.*;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;
import static java.util.stream.Collectors.toList;

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

    @Override
    public boolean existsProgressReservationByStoreId(Long storeId) {
        QStore store = QStore.store;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(reservation)
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .join(store).on(store.storeId.eq(storeDetail.storeId))
                .where(
                        store.storeId.eq(storeId),
                        reservation.status.eq(ReservationStatus.APPLIED)
                                .or(reservation.status.eq(ReservationStatus.APPROVED))
                )
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public boolean existsProgressReservationByStoreDetailId(Long storeDetailId) {
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(reservation)
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(
                        storeDetail.storeDetailId.eq(storeDetailId),
                        reservation.status.eq(ReservationStatus.APPLIED)
                                .or(reservation.status.eq(ReservationStatus.APPROVED))
                )
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public boolean existsProgressReservationByPartnerUserId(Long userId) {
        QUser user = QUser.user;
        QStore store = QStore.store;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(reservation)
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .join(store).on(store.storeId.eq(storeDetail.storeId))
                .join(user).on(user.userId.eq(store.userId))
                .where(
                        user.userId.eq(userId),
                        reservation.status.eq(ReservationStatus.APPLIED)
                                .or(reservation.status.eq(ReservationStatus.APPROVED))
                )
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public boolean existsProgressReservationByUserUserId(Long userId) {
        QReservation reservation = QReservation.reservation;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(reservation)
                .where(
                        reservation.userId.eq(userId),
                        reservation.status.eq(ReservationStatus.APPLIED)
                                .or(reservation.status.eq(ReservationStatus.APPROVED))
                )
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public Optional<ReservationDto.ReservationDetail> reservationDetailByReservationIdAndUserId(Long reservationId, Long userId) {
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QStore store = QStore.store;
        QUser user = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(ReservationDto.ReservationDetail.class,
                        reservation.createdAt,
                        Projections.fields(StoreDto.ForResponse.class,
                                store.storeId, store.name, store.location, store.description),
                        Projections.fields(ReservationDto.ReservationInfo.class,
                                reservation.reservationId, storeDetail.reservationTime, reservation.headCount, reservation.status)
                ))
                .from(reservation)
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.storeDetailId))
                .join(store).on(store.storeId.eq(storeDetail.storeId))
                .join(user).on(user.userId.eq(reservation.userId))
                .where(reservation.reservationId.eq(reservationId),
                        user.userId.eq(userId))
                .fetchOne());
    }

    @Override
    public List<ReservationDto.ListForUser> getUserReservationList(LocalDate date, Long userId) {
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QStore store = QStore.store;
        QUser user = QUser.user;

        Map<Long, ReservationDto.ListForUser> resultMap = jpaQueryFactory
                .from(user)
                .join(reservation).on(reservation.userId.eq(user.userId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.storeDetailId))
                .join(store).on(storeDetail.storeId.eq(store.storeId))
                .where(
                        user.userId.eq(userId),
                        storeDetail.reservationTime.between(
                                LocalDateTime.of(date, LocalTime.of(0, 0)),
                                LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0)))
                )
                .orderBy(storeDetail.reservationTime.asc())
                .transform(groupBy(storeDetail.storeDetailId).as(new QReservationDto_ListForUser(
                        storeDetail.reservationTime,
                        set(new QReservationDto_ForUser(
                                reservation.reservationId,
                                Projections.fields(StoreDto.ForResponse.class,
                                        store.storeId, store.name, store.location, store.description),
                                reservation.headCount, reservation.status)
                        ))
                ));

        return resultMap.keySet().stream()
                .map(resultMap::get)
                .collect(toList());
    }

    @Override
    public List<ReservationDto.ListForUser> getUserReservationList(ReservationStatus status, LocalDate date, Long userId) {
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QStore store = QStore.store;
        QUser user = QUser.user;

        Map<Long, ReservationDto.ListForUser> resultMap = jpaQueryFactory
                .from(user)
                .join(reservation).on(reservation.userId.eq(user.userId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.storeDetailId))
                .join(store).on(storeDetail.storeId.eq(store.storeId))
                .where(
                        user.userId.eq(userId),
                        storeDetail.reservationTime.between(
                                LocalDateTime.of(date, LocalTime.of(0, 0)),
                                LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0))
                        ),
                        reservation.status.eq(status)
                )
                .orderBy(storeDetail.reservationTime.asc())
                .transform(groupBy(storeDetail.storeDetailId).as(new QReservationDto_ListForUser(
                        storeDetail.reservationTime,
                        set(new QReservationDto_ForUser(
                                reservation.reservationId,
                                Projections.fields(StoreDto.ForResponse.class,
                                        store.storeId, store.name, store.location, store.description),
                                reservation.headCount, reservation.status)
                        ))
                ));

        return resultMap.keySet().stream()
                .map(resultMap::get)
                .collect(toList());
    }

    @Override
    public List<ReservationDto.ListForPartner> getPartnerReservationList(Long storeId, LocalDate date, Long userId) {
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QStore store = QStore.store;
        QUser storeOwner = QUser.user;
        QUser user = QUser.user;

        Map<Long, ReservationDto.ListForPartner> resultMap = jpaQueryFactory
                .from(storeOwner)
                .join(store).on(store.userId.eq(storeOwner.userId))
                .join(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .join(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .join(user).on(reservation.userId.eq(user.userId))
                .where(
                        store.storeId.eq(storeId),
                        store.userId.eq(userId),
                        storeDetail.reservationTime.between(
                                LocalDateTime.of(date, LocalTime.of(0, 0)),
                                LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0)))
                )
                .orderBy(storeDetail.reservationTime.asc())
                .transform(groupBy(storeDetail.storeDetailId).as(new QReservationDto_ListForPartner(
                        storeDetail.reservationTime,
                        set(new QReservationDto_ForPartner(
                                reservation.reservationId,
                                Projections.fields(AuthDto.ForResponse.class,
                                        user.userId, user.username),
                                reservation.headCount, reservation.status)
                        ))
                ));

        return resultMap.keySet().stream()
                .map(resultMap::get)
                .collect(toList());
    }

    @Override
    public List<ReservationDto.ListForPartner> getPartnerReservationList(Long storeId, ReservationStatus status, LocalDate date, Long userId) {
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QStore store = QStore.store;
        QUser storeOwner = QUser.user;
        QUser user = QUser.user;

        Map<Long, ReservationDto.ListForPartner> resultMap = jpaQueryFactory
                .from(storeOwner)
                .join(store).on(store.userId.eq(storeOwner.userId))
                .join(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .join(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .join(user).on(reservation.userId.eq(user.userId))
                .where(
                        store.storeId.eq(storeId),
                        store.userId.eq(userId),
                        storeDetail.reservationTime.between(
                                LocalDateTime.of(date, LocalTime.of(0, 0)),
                                LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0))
                        ),
                        reservation.status.eq(status)
                )
                .orderBy(storeDetail.reservationTime.asc())
                .transform(groupBy(storeDetail.storeDetailId).as(new QReservationDto_ListForPartner(
                        storeDetail.reservationTime,
                        set(new QReservationDto_ForPartner(
                                reservation.reservationId,
                                Projections.fields(AuthDto.ForResponse.class,
                                        user.userId, user.username),
                                reservation.headCount, reservation.status)
                        ))
                ));

        return resultMap.keySet().stream()
                .map(resultMap::get)
                .collect(toList());
    }

    @Override
    public List<ReservationDto.ListForPartner> getPartnerApplyReservationList(Long storeId, Long userId) {
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QStore store = QStore.store;
        QUser storeOwner = QUser.user;
        QUser user = QUser.user;

        Map<Long, ReservationDto.ListForPartner> resultMap = jpaQueryFactory
                .from(storeOwner)
                .join(store).on(store.userId.eq(storeOwner.userId))
                .join(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .join(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .join(user).on(reservation.userId.eq(user.userId))
                .where(
                        store.storeId.eq(storeId),
                        store.userId.eq(userId),
                        reservation.status.eq(ReservationStatus.APPLIED)
                )
                .orderBy(storeDetail.reservationTime.asc())
                .transform(groupBy(storeDetail.storeDetailId).as(new QReservationDto_ListForPartner(
                        storeDetail.reservationTime,
                        set(new QReservationDto_ForPartner(
                                reservation.reservationId,
                                Projections.fields(AuthDto.ForResponse.class,
                                        user.userId, user.username),
                                reservation.headCount, reservation.status)
                        ))
                ));

        return resultMap.keySet().stream()
                .map(resultMap::get)
                .collect(toList());
    }

    @Override
    public int countHeadCountByStoreDetailId(Long storeDetailId) {
        QReservation reservation = QReservation.reservation;

        return jpaQueryFactory
                .select(reservation.headCount.sum())
                .from(reservation)
                .where(
                        reservation.storeDetailId.eq(storeDetailId),
                        reservation.status.eq(ReservationStatus.APPLIED)
                                .or(reservation.status.eq(ReservationStatus.APPROVED))
                )
                .fetchFirst();
    }
}
