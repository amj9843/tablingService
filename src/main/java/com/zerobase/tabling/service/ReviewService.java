package com.zerobase.tabling.service;

import com.zerobase.tabling.data.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    //리뷰 등록 서비스
    ReviewDto.RegistResponse registReview(Long reservationId, Long userId, ReviewDto.RegistRequest request);
    
    //리뷰 수정 서비스
    void modifyReview(Long reviewId, Long userId, ReviewDto.ModifiedRequest request);
    
    //리뷰 삭제 서비스(유저 전용)
    void deleteReviewByUser(Long reviewId, Long userId);

    //리뷰 삭제 서비스(파트너 전용)
    void deleteReviewByPartner(Long reviewId, Long userId);
    
    //매장별 리뷰 목록 조회 서비스(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
    Page<ReviewDto.StoreReviewInfo> getReviewListByStore(Long storeId, Pageable pageable);

    //유저가 작성한 리뷰 목록 조회 서비스(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
    Page<ReviewDto.UserReviewInfo> getReviewListByUser(Long userId, Pageable pageable);

    //리뷰 상세 정보 조회 서비스
    ReviewDto.ReviewDetail getReviewDetail(Long reviewId);
}
