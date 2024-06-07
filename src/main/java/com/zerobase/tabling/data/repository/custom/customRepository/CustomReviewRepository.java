package com.zerobase.tabling.data.repository.custom.customRepository;

import com.zerobase.tabling.data.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomReviewRepository {
    //리뷰의 상세정보 조회
    Optional<ReviewDto.ReviewDetail> findReviewDetailByReviewId(Long reviewId);

    //매장별 리뷰목록 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
    Page<ReviewDto.StoreReviewInfo> reviewListByStoreId(Long storeId, Pageable pageable);

    //이용자별 리뷰목록 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
    Page<ReviewDto.UserReviewInfo> reviewListByUserId(Long userId, Pageable pageable);
}
