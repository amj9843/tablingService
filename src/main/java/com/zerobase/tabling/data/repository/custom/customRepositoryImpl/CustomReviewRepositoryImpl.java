package com.zerobase.tabling.data.repository.custom.customRepositoryImpl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.tabling.component.QueryDslUtil;
import com.zerobase.tabling.data.domain.QReservation;
import com.zerobase.tabling.data.domain.QReview;
import com.zerobase.tabling.data.domain.QStoreDetail;
import com.zerobase.tabling.data.domain.QUser;
import com.zerobase.tabling.data.dto.AuthDto;
import com.zerobase.tabling.data.dto.ReservationDto;
import com.zerobase.tabling.data.dto.ReviewDto;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;

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
                        Projections.fields(ReservationDto.ForResponse.class,
                                reservation.reservationId, storeDetail.reservationTime, reservation.headCount),
                        Projections.fields(AuthDto.ForResponse.class,
                                user.userId, user.username),
                        review.rate,
                        review.context
                ))
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.storeDetailId))
                .join(user).on(user.userId.eq(reservation.userId))
                .where(review.reviewId.eq(reviewId))
                .fetchOne());
    }

    @Override
    //매장별 리뷰목록 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
    public Page<ReviewDto.StoreReviewInfo> reviewListByStoreId(Long storeId, Pageable pageable) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        List<ReviewDto.StoreReviewInfo> reviewList = jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(storeDetail.storeId.eq(storeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(queryDslUtil.getAllOrderSpecifiers(pageable, review).toArray(OrderSpecifier[]::new))
                .transform(groupBy(storeDetail.storeId).list(Projections.fields(ReviewDto.StoreReviewInfo.class,
                        review.createdAt, review.updatedAt, review.reviewId, review.rate, review.context
                )));
        
        return new PageImpl<>(reviewList, pageable, reviewList.size());
    }

    @Override
    //이용자별 리뷰목록 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
    public Page<ReviewDto.UserReviewInfo> reviewListByUserId(Long userId, Pageable pageable) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        List<ReviewDto.UserReviewInfo> reviewList = jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(reservation.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(queryDslUtil.getAllOrderSpecifiers(pageable, review).toArray(OrderSpecifier[]::new))
                .transform(groupBy(reservation.userId).list(Projections.fields(ReviewDto.UserReviewInfo.class,
                        review.createdAt, review.updatedAt,
                        storeDetail.storeId, review.reviewId, review.rate, review.context
                )));

        return new PageImpl<>(reviewList, pageable, reviewList.size());
    }
}
