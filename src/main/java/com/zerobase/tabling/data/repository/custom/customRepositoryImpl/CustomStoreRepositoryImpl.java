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

import static com.querydsl.core.group.GroupBy.groupBy;

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
                .join(storeDetail)
                .on(
                        reservation.storeDetailId.eq(storeDetail.storeDetailId)
                )
                .join(store)
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
                .from(store)
                .join(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .join(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .join(review).on(review.reservationId.eq(reservation.reservationId))
                .transform(groupBy(store.storeId).list(Projections.fields(StoreDto.StoreInfo.class,
                        store.storeId,
                        store.name,
                        store.location,
                        store.description,
                        review.rate.avg(),
                        review.count()
                )));
    }

    @Override
    public List<StoreDto.StoreInfo> getStoreList(String word) {
        QStore store = QStore.store;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;
        QReview review = QReview.review;

        return jpaQueryFactory
                .from(store)
                .join(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .join(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .join(review).on(review.reservationId.eq(reservation.reservationId))
                .where(store.name.contains(word).or(store.location.contains(word)).or(store.description.contains(word)))
                .transform(groupBy(store.storeId).list(Projections.fields(StoreDto.StoreInfo.class,
                        store.storeId,
                        store.name,
                        store.location,
                        store.description,
                        review.rate.avg(),
                        review.count()
                )));
    }

    @Override
    public List<StoreDto.StoreInfo> getStoreListByPartner(Long userId) {
        QStore store = QStore.store;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;
        QReview review = QReview.review;

        return jpaQueryFactory
                .from(store)
                .join(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .join(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .join(review).on(review.reservationId.eq(reservation.reservationId))
                .where(store.userId.eq(userId))
                .transform(groupBy(store.storeId).list(Projections.fields(StoreDto.StoreInfo.class,
                        store.storeId,
                        store.name,
                        store.location,
                        store.description,
                        review.rate.avg(),
                        review.count()
                )));
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
                        review.rate.avg(),
                        review.count()
                ))
                .from(store)
                .join(storeDetail).on(storeDetail.storeId.eq(store.storeId))
                .join(reservation).on(reservation.storeDetailId.eq(reservation.reservationId))
                .join(review).on(review.reservationId.eq(reservation.reservationId))
                .where(store.storeId.eq(storeId))
                .fetchOne());
    }
}
