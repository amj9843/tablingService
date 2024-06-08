package com.zerobase.tabling.data.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.tabling.annotation.ReservationTime;
import com.zerobase.tabling.data.domain.StoreDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class StoreDetailDto {
    //매장 상세정보 등록 요청 DTO List
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistRequests {
        @NotEmpty(message = "요청 배열 안은 비울 수 없습니다.") @Valid
        private List<RegistRequest> storeDetails;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistRequest {
        @ReservationTime(message = "예약 가능한 시간을 필수로 입력해야 합니다." +
                " 단, 현재 시간으로부터 1시간 이내의 시간은 등록할 수 없습니다." +
                " 시간 입력 형식 -> 'yyyy-MM-dd HH:mm'")
        private String reservationTime;
        @Min(value = 1, message = "예약 인원 수는 최소 한 명 입니다.")
        private int headCount;

        public StoreDetail toEntity(Long storeId, LocalDateTime time){
            return StoreDetail.builder()
                    .storeId(storeId)
                    .reservationTime(time)
                    .headCount(this.headCount)
                    .build();
        }
    }

    //매장에 등록한 상세 정보 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistedStoreDetails {
        private Long storeId;
        private List<RegistedStoreDetail> registedStoreDetails;
    }

    //등록한 상세 정보 DTO
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"storeDetailId"})
    public static class RegistedStoreDetail {
        private Long storeDetailId;
        private LocalDateTime reservationTime;
        private int headCount;

        public static RegistedStoreDetail fromEntity(StoreDetail storeDetail) {
            return RegistedStoreDetail.builder()
                    .storeDetailId(storeDetail.getStoreDetailId())
                    .reservationTime(storeDetail.getReservationTime())
                    .headCount(storeDetail.getHeadCount())
                    .build();
        }
    }

    /**
     * 매장 상세정보 수정 요청 DTO
     * 수정 가능 정보 : 인원 수
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifiedRequest {
        @Min(value = 1, message = "예약 인원 수는 최소 한 명 입니다.")
        private int headCount;
    }

    @Data
    @NoArgsConstructor
    //예약 신청, 수정 시 유효성 검토용
    public static class CustomStoreDetail {
        private Long storeDetailId;
        private Long storeId;
        private LocalDateTime reservationTime;
        private int headCount;

        @QueryProjection
        public CustomStoreDetail(Long storeDetailId,
                                 Long storeId,
                                 LocalDateTime reservationTime,
                                 int headCount) {
            this.storeDetailId = storeDetailId;
            this.storeId = storeId;
            this.reservationTime = reservationTime;
            this.headCount = headCount;
        }
    }

    @Data
    @NoArgsConstructor
    //예약 상세 내역 조회 시 반환 DTO
    public static class Detail {
        private Long storeDetailId;
        private LocalDateTime reservationTime;
        private int totalHeadCount;
        private int nowHeadCount;

        @QueryProjection
        public Detail(Long storeDetailId, LocalDateTime reservationTime, int totalHeadCount, int nowHeadCount) {
            this.storeDetailId = storeDetailId;
            this.reservationTime = reservationTime;
            this.totalHeadCount = totalHeadCount;
            this.nowHeadCount = nowHeadCount;
        }
    }
}
