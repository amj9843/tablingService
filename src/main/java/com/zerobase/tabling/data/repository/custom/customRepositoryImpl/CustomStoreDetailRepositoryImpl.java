package com.zerobase.tabling.data.repository.custom.customRepositoryImpl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.QReservation;
import com.zerobase.tabling.data.domain.QStore;
import com.zerobase.tabling.data.domain.QStoreDetail;
import com.zerobase.tabling.data.dto.StoreDetailDto;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomStoreDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class CustomStoreDetailRepositoryImpl implements CustomStoreDetailRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<StoreDetailDto.CustomStoreDetail> findByStoreDetailIdForReservation(Long storeDetailId) {
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;

        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(StoreDetailDto.CustomStoreDetail.class,
                        storeDetail.storeDetailId,
                        storeDetail.storeId,
                        storeDetail.reservationTime,
                        storeDetail.headCount.add(reservation.headCount.sum().multiply(-1))))
                .from(storeDetail)
                .join(reservation)
                .on(
                        storeDetail.storeDetailId.eq(reservation.storeDetailId)
                ).where(
                        storeDetail.storeDetailId.eq(storeDetailId),
                        reservation.status.eq(ReservationStatus.APPLIED)
                                .or(reservation.status.eq(ReservationStatus.APPROVED))
                )
                .groupBy(storeDetail.storeDetailId)
                .fetchOne());
    }

    @Override
    public boolean existsFindByUserIdAndStoreDetailId(Long userId, Long storeDetailId) {
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QStore store = QStore.store;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(storeDetail)
                .join(store)
                .on(
                        storeDetail.storeId.eq(store.storeId)
                )
                .where(
                        storeDetail.storeDetailId.eq(storeDetailId).and(
                                store.userId.eq(userId)
                        )
                )
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public Page<StoreDetailDto.Detail> getDetailsByStoreId(Long storeId, Pageable pageable, LocalDateTime now) {
        QStore store = QStore.store;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        List<StoreDetailDto.Detail> details = jpaQueryFactory
                .from(storeDetail)
                .join(store).on(store.storeId.eq(storeDetail.storeId))
                .join(reservation).on(reservation.storeDetailId.eq(storeDetail.storeDetailId))
                .where(store.storeId.eq(storeId),
                        storeDetail.reservationTime.after(now),
                        reservation.status.eq(ReservationStatus.APPROVED)
                                .or(reservation.status.eq(ReservationStatus.APPLIED)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(storeDetail.reservationTime.asc())
                .transform(groupBy(storeDetail.storeDetailId).list(Projections.fields(StoreDetailDto.Detail.class,
                        storeDetail.storeDetailId, storeDetail.reservationTime, storeDetail.headCount, reservation.headCount.sum()
                )));

        return new PageImpl<>(details, pageable, details.size());
    }
}
