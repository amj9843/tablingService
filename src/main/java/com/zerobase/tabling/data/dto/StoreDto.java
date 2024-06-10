package com.zerobase.tabling.data.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.tabling.annotation.NullOrNot;
import com.zerobase.tabling.data.domain.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

public class StoreDto {
    //매장등록 요청 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistRequest {
        @NotBlank(message = "매장명 입력은 필수이며 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String name;
        @NotBlank(message = "주소 입력은 필수이며 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String location;
        @NotNull
        private String description;

        public Store toEntity(Long userId){
            return Store.builder()
                    .userId(userId)
                    .name(this.name)
                    .location(this.location)
                    .description(this.description)
                    .build();
        }
    }

    //매장 등록 정보 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistedStoreInfo {
        private Long ownerId;
        private Long storeId;
        private String name;
        private String location;
        private String description;
    }

    /**
     * 매장 정보 수정 요청 DTO
     * 수정 가능 정보 : 매장명, 장소, 설명
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifiedRequest {
        @NullOrNot(message = "매장명이 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String name;
        @NullOrNot(message = "주소가 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String location;
        private String description;
    }

    //매장 예약 내역 조회, 리뷰 조회 등 다른 조회에서 매장 정보를 보여야할 때 사용될 응답 DTO
    @Data
    @NoArgsConstructor
    @Builder
    public static class ForResponse {
        private Long storeId;
        private String name;
        private String location;
        private String description;

        @QueryProjection
        public ForResponse(Long storeId, String name, String location, String description) {
            this.storeId = storeId;
            this.name = name;
            this.location = location;
            this.description = description;
        }
    }

    //매장 목록 조회 시 DTO
    @Data
    @NoArgsConstructor
    public static class StoreInfo {
        private Long storeId;
        private String name;
        private String location;
        private String description;
        private Double rate;
        private Long reviewCount;

        @QueryProjection
        public StoreInfo(Long storeId, String name, String location, String description,
                         Double rate, Long reviewCount) {
            this.storeId = storeId;
            this.name = name;
            this.location = location;
            this.description = description;
            this.rate = rate;
            this.reviewCount = reviewCount;
        }
    }

    //매장 상세 DTO
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private Long storeId;
        private String name;
        private String location;
        private String description;
        private Double rate;
        private Long reviewCount;
        private Page<StoreDetailDto.Detail> details;

        public static Detail toDetail(StoreInfo storeInfo, Page<StoreDetailDto.Detail> details) {
            return Detail.builder()
                    .storeId(storeInfo.getStoreId())
                    .name(storeInfo.getName())
                    .location(storeInfo.getLocation())
                    .description(storeInfo.getDescription())
                    .rate(storeInfo.getRate())
                    .reviewCount(storeInfo.getReviewCount())
                    .details(details)
                    .build();
        }
    }
}
