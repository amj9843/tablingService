package com.zerobase.tabling.data.repository.custom.customRepositoryImpl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.tabling.component.QueryDslUtil;
import com.zerobase.tabling.data.domain.QReservation;
import com.zerobase.tabling.data.domain.QReview;
import com.zerobase.tabling.data.domain.QStoreDetail;
import com.zerobase.tabling.data.domain.QUser;
import com.zerobase.tabling.data.dto.QAuthDto_ForResponse;
import com.zerobase.tabling.data.dto.QReservationDto_ForResponse;
import com.zerobase.tabling.data.dto.ReviewDto;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QueryDslUtil queryDslUtil;

    @Override
    //리뷰의 상세정보 조회
    public Optional<ReviewDto.ReviewDetail> findReviewDetailByReviewId(Long reviewId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;
        QUser user = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(ReviewDto.ReviewDetail.class,
                        review.createdAt,
                        review.updatedAt,
                        new QReservationDto_ForResponse(
                                reservation.reservationId, storeDetail.reservationTime, reservation.headCount)
                                .as("reservation"),
                        new QAuthDto_ForResponse(user.userId, user.username).as("user"),
                        review.rate,
                        review.context
                ))
                .from(review)
                .leftJoin(reservation).on(reservation.reservationId.eq(review.reservationId))
                .leftJoin(storeDetail).on(storeDetail.storeDetailId.eq(reservation.storeDetailId))
                .leftJoin(user).on(user.userId.eq(reservation.userId))
                .where(review.reviewId.eq(reviewId))
                .fetchOne());
    }

    @Override
    //매장별 리뷰목록 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
    public Page<ReviewDto.StoreReviewInfo> reviewListByStoreId(Long storeId, Pageable pageable) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        Map<Long, ReviewDto.StoreReviewInfo> reviewMap = jpaQueryFactory
                .from(review)
                .leftJoin(reservation).on(reservation.reservationId.eq(review.reservationId))
                .leftJoin(storeDetail).on(storeDetail.storeDetailId.eq(reservation.storeDetailId))
                .where(storeDetail.storeId.eq(storeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(queryDslUtil.getAllOrderSpecifiers(pageable, review).toArray(OrderSpecifier[]::new))
                .transform(groupBy(storeDetail.storeId).as(Projections.fields(ReviewDto.StoreReviewInfo.class,
                        review.createdAt, review.updatedAt, review.reviewId, review.rate, review.context
                )));

        List<ReviewDto.StoreReviewInfo> reviewList = reviewMap.keySet().stream()
                .map(reviewMap::get)
                .collect(toList());
        
        return new PageImpl<>(reviewList, pageable, reviewList.size());
    }

    @Override
    //이용자별 리뷰목록 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
    public Page<ReviewDto.UserReviewInfo> reviewListByUserId(Long userId, Pageable pageable) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        Map<Long, ReviewDto.UserReviewInfo> reviewMap = jpaQueryFactory
                .from(review)
                .leftJoin(reservation).on(reservation.reservationId.eq(review.reservationId))
                .leftJoin(storeDetail).on(storeDetail.storeDetailId.eq(reservation.storeDetailId))
                .where(reservation.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(queryDslUtil.getAllOrderSpecifiers(pageable, review).toArray(OrderSpecifier[]::new))
                .transform(groupBy(reservation.userId).as(Projections.fields(ReviewDto.UserReviewInfo.class,
                        review.createdAt, review.updatedAt,
                        storeDetail.storeId, review.reviewId, review.rate, review.context
                )));

        List<ReviewDto.UserReviewInfo> reviewList = reviewMap.keySet().stream()
                .map(reviewMap::get)
                .collect(toList());

        return new PageImpl<>(reviewList, pageable, reviewList.size());
    }
}
