package com.zerobase.tabling.data.repository;

import com.zerobase.tabling.data.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    //사용자가 해당 예약에 이미 작성한 리뷰가 있는지 확인
    boolean existsByReservationId(Long reservationId);

    //리뷰아이디로 리뷰 호출
    Optional<Review> findByReviewId(Long reviewId);
}
