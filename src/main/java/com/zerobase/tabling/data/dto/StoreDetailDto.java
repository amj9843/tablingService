package com.zerobase.tabling.data.dto;

import com.zerobase.tabling.annotation.ReservationTime;
import com.zerobase.tabling.data.domain.Store;
import com.zerobase.tabling.data.domain.StoreDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        public StoreDetail toEntity(Store store, LocalDateTime time){
            return StoreDetail.builder()
                    .store(store)
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
    @AllArgsConstructor
    @NoArgsConstructor
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
    public static class ModifiedInfoRequest {
        @Min(value = 1, message = "예약 인원 수는 최소 한 명 입니다.")
        private int headCount;
    }
}
