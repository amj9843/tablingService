package com.zerobase.tabling.service;

import com.zerobase.tabling.data.dto.ReviewDto;

public interface ReviewService {
    //리뷰 등록 서비스
    ReviewDto.RegistResponse registReview(Long reservationId, Long userId, ReviewDto.RegistRequest request);
    
    //리뷰 수정 서비스
    void modifyReview(Long reviewId, Long userId, ReviewDto.ModifiedRequest request);
    
    //리뷰 삭제 서비스(유저 전용)
    void deleteReviewByUser(Long reviewId, Long userId);

    //리뷰 삭제 서비스(파트너 전용)
    void deleteReviewByPartner(Long reviewId, Long userId);
    
    //매장별 리뷰 목록 조회 서비스
    
    //자신이 등록한 리뷰 목록 조회 서비스
    
    //리뷰 상세 조회 서비스
}
