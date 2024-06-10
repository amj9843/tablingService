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

@Repository
@RequiredArgsConstructor
public class CustomStoreDetailRepositoryImpl implements CustomStoreDetailRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<StoreDetailDto.CustomStoreDetail> findByStoreDetailIdForReservation(Long storeDetailId) {
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QReservation reservation = QReservation.reservation;

        StoreDetailDto.CustomStoreDetail detail = jpaQueryFactory
                .select(Projections.fields(StoreDetailDto.CustomStoreDetail.class,
                        storeDetail.storeDetailId,
                        storeDetail.storeId,
                        storeDetail.reservationTime,
                        storeDetail.headCount))
                .from(storeDetail)
                .where(
                        storeDetail.storeDetailId.eq(storeDetailId)
                )
                .fetchOne();

        if (detail != null) {
            Integer nowHeadCount = jpaQueryFactory
                    .select(reservation.headCount.sum().coalesce(0))
                    .from(reservation)
                    .where(reservation.storeDetailId.eq(detail.getStoreDetailId()),
                            reservation.status.eq(ReservationStatus.APPLIED).or(reservation.status.eq(ReservationStatus.APPROVED)))
                    .groupBy(reservation.storeDetailId)
                    .fetchFirst();

            if (nowHeadCount == null) nowHeadCount = 0;

            detail.setHeadCount(detail.getHeadCount() - nowHeadCount);
        }

        return Optional.ofNullable(detail);
    }

    @Override
    public boolean existsFindByUserIdAndStoreDetailId(Long userId, Long storeDetailId) {
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QStore store = QStore.store;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(storeDetail)
                .leftJoin(store)
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

        List<StoreDetailDto.Detail> detailList = jpaQueryFactory
                .select(Projections.fields(StoreDetailDto.Detail.class,
                                storeDetail.storeDetailId, storeDetail.reservationTime,
                                storeDetail.headCount.as("totalHeadCount")))
                .from(storeDetail)
                .leftJoin(store).on(store.storeId.eq(storeDetail.storeId))
                .where(store.storeId.eq(storeId),
                        storeDetail.reservationTime.after(now))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(storeDetail.reservationTime.asc())
                .groupBy(storeDetail.storeDetailId)
                .fetch();

        for (StoreDetailDto.Detail detail: detailList) {
            Integer nowHeadCount = jpaQueryFactory
                    .select(reservation.headCount.sum().coalesce(0))
                    .from(reservation)
                    .where(reservation.storeDetailId.eq(detail.getStoreDetailId()),
                            reservation.status.eq(ReservationStatus.APPLIED).or(reservation.status.eq(ReservationStatus.APPROVED)))
                    .groupBy(reservation.storeDetailId)
                    .fetchFirst();

            if (nowHeadCount == null) nowHeadCount = 0;

            detail.setNowHeadCount(nowHeadCount);
        }

        return new PageImpl<>(detailList, pageable, detailList.size());
    }
}
