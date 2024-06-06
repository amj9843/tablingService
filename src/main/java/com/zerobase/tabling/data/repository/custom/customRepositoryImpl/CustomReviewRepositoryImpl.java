package com.zerobase.tabling.data.repository.custom.customRepositoryImpl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.tabling.data.domain.QReservation;
import com.zerobase.tabling.data.domain.QReview;
import com.zerobase.tabling.data.domain.QStoreDetail;
import com.zerobase.tabling.data.domain.QUser;
import com.zerobase.tabling.data.dto.AuthDto;
import com.zerobase.tabling.data.dto.ReservationDto;
import com.zerobase.tabling.data.dto.ReviewDto;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {
    private final JPAQueryFactory jpaQueryFactory;

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
    //매장별 리뷰목록 조회(최근 등록순)
    public List<ReviewDto.StoreReviewInfo> reviewListByStoreIdOrderByLatest(Long storeId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        return jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(storeDetail.storeId.eq(storeId))
                .orderBy(review.createdAt.desc())
                .transform(groupBy(storeDetail.storeId).list(Projections.fields(ReviewDto.StoreReviewInfo.class,
                        review.createdAt, review.updatedAt, review.reviewId, review.rate, review.context
                )));
    }

    @Override
    //매장별 리뷰목록 조회(등록순)
    public List<ReviewDto.StoreReviewInfo> reviewListByStoreIdOrderByEarliest(Long storeId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        return jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(storeDetail.storeId.eq(storeId))
                .orderBy(review.createdAt.asc())
                .transform(groupBy(storeDetail.storeId).list(Projections.fields(ReviewDto.StoreReviewInfo.class,
                        review.createdAt, review.updatedAt, review.reviewId, review.rate, review.context
                )));
    }

    @Override
    //매장별 리뷰목록 조회(별점 높은순)
    public List<ReviewDto.StoreReviewInfo> reviewListByStoreIdOrderByHighRate(Long storeId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        return jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(storeDetail.storeId.eq(storeId))
                .orderBy(review.rate.desc(), review.createdAt.desc())
                .transform(groupBy(storeDetail.storeId).list(Projections.fields(ReviewDto.StoreReviewInfo.class,
                        review.createdAt, review.updatedAt, review.reviewId, review.rate, review.context
                )));
    }

    @Override
    //매장별 리뷰목록 조회(별점 낮은순)
    public List<ReviewDto.StoreReviewInfo> reviewListByStoreIdOrderByLowRate(Long storeId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        return jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(storeDetail.storeId.eq(storeId))
                .orderBy(review.rate.asc(), review.createdAt.desc())
                .transform(groupBy(storeDetail.storeId).list(Projections.fields(ReviewDto.StoreReviewInfo.class,
                        review.createdAt, review.updatedAt, review.reviewId, review.rate, review.context
                )));
    }

    @Override
    //이용자별 리뷰목록 조회(최근 등록순)
    public List<ReviewDto.UserReviewInfo> reviewListByUserIdOrderByLatest(Long userId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        return jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(reservation.userId.eq(userId))
                .orderBy(review.createdAt.desc())
                .transform(groupBy(reservation.userId).list(Projections.fields(ReviewDto.UserReviewInfo.class,
                        review.createdAt, review.updatedAt,
                        storeDetail.storeId, review.reviewId, review.rate, review.context
                )));
    }

    @Override
    //이용자별 리뷰목록 조회(등록순)
    public List<ReviewDto.UserReviewInfo> reviewListByUserIdOrderByEarliest(Long userId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        return jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(reservation.userId.eq(userId))
                .orderBy(review.createdAt.asc())
                .transform(groupBy(reservation.userId).list(Projections.fields(ReviewDto.UserReviewInfo.class,
                        review.createdAt, review.updatedAt,
                        storeDetail.storeId, review.reviewId, review.rate, review.context
                )));
    }

    @Override
    //이용자별 리뷰목록 조회(별점 높은순/최근 등록순)
    public List<ReviewDto.UserReviewInfo> reviewListByUserIdOrderByHighRate(Long userId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        return jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(reservation.userId.eq(userId))
                .orderBy(review.rate.desc(), review.createdAt.desc())
                .transform(groupBy(reservation.userId).list(Projections.fields(ReviewDto.UserReviewInfo.class,
                        review.createdAt, review.updatedAt,
                        storeDetail.storeId, review.reviewId, review.rate, review.context
                )));
    }

    @Override
    //이용자별 리뷰목록 조회(별점 낮은순/최근 등록순)
    public List<ReviewDto.UserReviewInfo> reviewListByUserIdOrderByLowRate(Long userId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStoreDetail storeDetail = QStoreDetail.storeDetail;

        return jpaQueryFactory
                .from(review)
                .join(reservation).on(reservation.reservationId.eq(review.reservationId))
                .join(storeDetail).on(storeDetail.storeDetailId.eq(reservation.reservationId))
                .where(reservation.userId.eq(userId))
                .orderBy(review.rate.asc(), review.createdAt.desc())
                .transform(groupBy(reservation.userId).list(Projections.fields(ReviewDto.UserReviewInfo.class,
                        review.createdAt, review.updatedAt,
                        storeDetail.storeId, review.reviewId, review.rate, review.context
                )));
    }
}
