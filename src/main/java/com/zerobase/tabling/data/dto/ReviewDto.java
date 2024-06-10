package com.zerobase.tabling.data.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.tabling.data.domain.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReviewDto {
    //리뷰 작성 요청 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistRequest {
        //평점(정수만 허용)
        @Min(value = 1, message = "평점 입력은 필수이며 최소 1점은 주어야 합니다.")
        @Max(value = 5, message = "평점은 5를 초과할 수 없습니다.")
        private int rate;
        //리뷰 내용
        private String context;

        public Review toEntity(Long reservationId){
            return Review.builder()
                    .reservationId(reservationId)
                    .rate(this.rate)
                    .context(this.context)
                    .build();
        }
    }

    /**
     * 리뷰 수정 요청 DTO
     * 수정 가능 정보 : 평점, 내용
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifiedRequest {
        //평점(정수만 허용)
        @Max(value = 5, message = "평점은 5를 초과할 수 없습니다.")
        private int rate;
        //리뷰 내용
        private String context;
    }

    //리뷰 등록 응답 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistResponse {
        private Long reviewId;
        private int rate;
        private String context;
    }

    //유저별 리뷰 조회 응답 DTO
    @Data
    @NoArgsConstructor
    public static class UserReviewInfo {
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long storeId;
        private Long reviewId;
        private int rate;
        private String context;

        @QueryProjection
        public UserReviewInfo(LocalDateTime createdAt, LocalDateTime updatedAt,
                              Long storeId, Long reviewId, int rate, String context) {
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.storeId = storeId;
            this.reviewId = reviewId;
            this.rate = rate;
            this.context = context;
        }
    }

    //상점별 리뷰 조회 응답 DTO
    @Data
    @NoArgsConstructor
    public static class StoreReviewInfo {
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long reviewId;
        private int rate;
        private String context;

        @QueryProjection
        public StoreReviewInfo(LocalDateTime createdAt, LocalDateTime updatedAt,
                               Long reviewId, int rate, String context) {
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.reviewId = reviewId;
            this.rate = rate;
            this.context = context;
        }
    }

    //리뷰 상세 조회 응답 DTO
    @Data
    @NoArgsConstructor
    public static class ReviewDetail {
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private ReservationDto.ForResponse reservation;
        private AuthDto.ForResponse user;
        private int rate;
        private String context;

        @QueryProjection
        public ReviewDetail(LocalDateTime createdAt, LocalDateTime updatedAt,
                            ReservationDto.ForResponse reservation, AuthDto.ForResponse user,
                            int rate, String context) {
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.reservation = reservation;
            this.user = user;
            this.rate = rate;
            this.context = context;
        }
    }
}
