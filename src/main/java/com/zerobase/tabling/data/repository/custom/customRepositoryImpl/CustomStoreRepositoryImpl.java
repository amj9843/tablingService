package com.zerobase.tabling.data.repository.custom.customRepositoryImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.tabling.data.domain.QReservation;
import com.zerobase.tabling.data.domain.QStore;
import com.zerobase.tabling.data.domain.QStoreDetail;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
