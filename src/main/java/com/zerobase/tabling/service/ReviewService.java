package com.zerobase.tabling.service;

import com.zerobase.tabling.data.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    //리뷰 등록 서비스
    ReviewDto.RegistResponse registReview(Long reservationId, Long userId, ReviewDto.RegistRequest request);
    
    //리뷰 수정 서비스
    void modifyReview(Long reviewId, Long userId, ReviewDto.ModifiedRequest request);
    
    //리뷰 삭제 서비스(유저 전용)
    void deleteReviewByUser(Long reviewId, Long userId);

    //리뷰 삭제 서비스(파트너 전용)
    void deleteReviewByPartner(Long reviewId, Long userId);
    
    //매장별 리뷰 목록 조회 서비스(기본: 최근 등록순)
    List<ReviewDto.StoreReviewInfo> getReviewListByStoreLatest(Long storeId);

    //매장별 리뷰 목록 조회 서비스(등록순)
    List<ReviewDto.StoreReviewInfo> getReviewListByStoreEarliest(Long storeId);

    //매장별 리뷰 목록 조회 서비스(별점 높은순)
    List<ReviewDto.StoreReviewInfo> getReviewListByStoreHighRate(Long storeId);

    //매장별 리뷰 목록 조회 서비스(별점 낮은순)
    List<ReviewDto.StoreReviewInfo> getReviewListByStoreLowRate(Long storeId);
    
    //유저가 작성한 리뷰 목록 조회 서비스(기본: 최근 등록순)
    List<ReviewDto.UserReviewInfo> getReviewListByUserLatest(Long userId);

    //유저가 작성한 리뷰 목록 조회 서비스(등록순)
    List<ReviewDto.UserReviewInfo> getReviewListByUserEarliest(Long userId);

    //유저가 작성한 리뷰 목록 조회 서비스(별점 높은순/최근 등록순)
    List<ReviewDto.UserReviewInfo> getReviewListByUserHighRate(Long userId);

    //유저가 작성한 리뷰 목록 조회 서비스(기본: 별점 낮은순/최근 등록순)
    List<ReviewDto.UserReviewInfo> getReviewListByUserLowRate(Long userId);
    
    //리뷰 상세 정보 조회 서비스
    ReviewDto.ReviewDetail getReviewDetail(Long reviewId);
}
