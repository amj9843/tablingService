package com.zerobase.tabling.data.repository.custom.customRepository;

import com.zerobase.tabling.data.dto.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface CustomReviewRepository {
    //리뷰의 상세정보 조회
    Optional<ReviewDto.ReviewDetail> findReviewDetailByReviewId(Long reviewId);

    //매장별 리뷰목록 조회(최근 등록순)
    List<ReviewDto.StoreReviewInfo> reviewListByStoreIdOrderByLatest(Long storeId);

    //매장별 리뷰목록 조회(등록순)
    List<ReviewDto.StoreReviewInfo> reviewListByStoreIdOrderByEarliest(Long storeId);

    //매장별 리뷰목록 조회(별점 높은순)
    List<ReviewDto.StoreReviewInfo> reviewListByStoreIdOrderByHighRate(Long storeId);

    //매장별 리뷰목록 조회(별점 낮은순)
    List<ReviewDto.StoreReviewInfo> reviewListByStoreIdOrderByLowRate(Long storeId);

    //이용자별 리뷰목록 조회(최근 등록순)
    List<ReviewDto.UserReviewInfo> reviewListByUserIdOrderByLatest(Long userId);

    //이용자별 리뷰목록 조회(등록순)
    List<ReviewDto.UserReviewInfo> reviewListByUserIdOrderByEarliest(Long userId);

    //이용자별 리뷰목록 조회(별점 높은순)
    List<ReviewDto.UserReviewInfo> reviewListByUserIdOrderByHighRate(Long userId);

    //이용자별 리뷰목록 조회(별점 낮은순)
    List<ReviewDto.UserReviewInfo> reviewListByUserIdOrderByLowRate(Long userId);
}
