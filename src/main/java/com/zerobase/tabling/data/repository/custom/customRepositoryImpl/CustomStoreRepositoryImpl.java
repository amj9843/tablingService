package com.zerobase.tabling.data.repository.custom.customRepositoryImpl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.tabling.data.domain.QReservation;
import com.zerobase.tabling.data.domain.QReview;
import com.zerobase.tabling.data.domain.QStore;
import com.zerobase.tabling.data.domain.QStoreDetail;
import com.zerobase.tabling.data.dto.StoreDto;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomStoreRepositoryImpl implements CustomStoreRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByReservationIdAndUserId(Long reservationId, Long userId) {
        QStore store = QStore.store;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(reservation)
                .leftJoin(storeDetail)
                .on(
                        reservation.storeDetailId.eq(storeDetail.storeDetailId)
                )
                .leftJoin(store)
                .on(
                        storeDetail.storeId.eq(store.storeId)
                )
                .where(
                        reservation.reservationId.eq(reservationId),
                        store.userId.eq(userId)
                )
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public List<StoreDto.StoreInfo> getStoreList() {
        QStore store = QStore.store;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;
        QReview review = QReview.review;

        return jpaQueryFactory
                .select(Projections.fields(StoreDto.StoreInfo.class,
                        store.storeId,
                        store.name,
                        store.location,
                        store.description,
                        review.rate.avg().coalesce(0.0).as("rate"),
                        review.count().coalesce(0L).as("reviewCount")))
                .from(store)
                .leftJoin(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .leftJoin(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .leftJoin(review).on(review.reservationId.eq(reservation.reservationId))
                .groupBy(store.storeId)
                .fetch();
    }

    @Override
    public List<StoreDto.StoreInfo> getStoreList(String word) {
        QStore store = QStore.store;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;
        QReview review = QReview.review;

        return jpaQueryFactory
                .select(Projections.fields(StoreDto.StoreInfo.class,
                        store.storeId,
                        store.name,
                        store.location,
                        store.description,
                        review.rate.avg().coalesce(0.0).as("rate"),
                        review.count().coalesce(0L).as("reviewCount")))
                .from(store)
                .leftJoin(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .leftJoin(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .leftJoin(review).on(review.reservationId.eq(reservation.reservationId))
                .where(store.name.contains(word).or(store.location.contains(word)).or(store.description.contains(word)))
                .groupBy(store.storeId)
                .fetch();
    }

    @Override
    public List<StoreDto.StoreInfo> getStoreListByPartner(Long userId) {
        QStore store = QStore.store;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;
        QReview review = QReview.review;

        return jpaQueryFactory
                .select(Projections.fields(StoreDto.StoreInfo.class,
                        store.storeId,
                        store.name,
                        store.location,
                        store.description,
                        review.rate.avg().coalesce(0.0).as("rate"),
                        review.count().coalesce(0L).as("reviewCount")))
                .from(store)
                .leftJoin(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .leftJoin(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .leftJoin(review).on(review.reservationId.eq(reservation.reservationId))
                .where(store.userId.eq(userId))
                .groupBy(store.storeId)
                .fetch();
    }

    @Override
    public Optional<StoreDto.StoreInfo> getStoreInfoByStoreId(Long storeId) {
        QStore store = QStore.store;
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(StoreDto.StoreInfo.class,
                        store.storeId,
                        store.name,
                        store.location,
                        store.description,
                        review.rate.avg().coalesce(0.0).as("rate"),
                        review.count().coalesce(0L).as("reviewCount")))
                .from(store)
                .leftJoin(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .leftJoin(reservation).on(reservation.storeDetailId.eq(reservation.reservationId))
                .leftJoin(review).on(review.reservationId.eq(reservation.reservationId))
                .where(store.storeId.eq(storeId))
                .groupBy(store.storeId)
                .fetchOne());
    }
}
