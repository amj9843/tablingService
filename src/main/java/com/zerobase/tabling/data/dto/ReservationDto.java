package com.zerobase.tabling.data.dto;

import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.Reservation;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReservationDto {
    //매장 예약 신청 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplyRequest {
        @Min(value = 1, message = "매장 상세정보 식별번호 입력이 필요합니다.")
        private Long storeDetailId;
        @Min(value = 1, message = "예약 인원수는 최소 1명입니다.")
        private int headCount;

        public Reservation toEntity(Long userId){
            return Reservation.builder()
                    .userId(userId)
                    .storeDetailId(this.storeDetailId)
                    .headCount(this.headCount)
                    .status(ReservationStatus.APPROVED)
                    .build();
        }
    }

    //매장 예약 정보 수정 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifyRequest {
            private Long storeDetailId;
            private int headCount;
    }

    //예약번호를 이용한 키오스크 방문 완료 요청 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KioskReservationIdRequest {
        @Min(value = 1, message = "예약 번호는 반드시 입력하여야 합니다.")
        private Long reservationId;
    }

    //예약 정보 응답 DTO
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationInfo {
        private Long reservationId;
        private LocalDateTime time;
        private int headCount;
        private ReservationStatus status;

        public ReservationInfo fromEntity(Reservation reservation,
                                          StoreDetailDto.CustomStoreDetail storeDetail) {
            return ReservationInfo.builder()
                    .reservationId(reservation.getReservationId())
                    .time(storeDetail.getReservationTime())
                    .headCount(reservation.getHeadCount())
                    .status(reservation.getStatus())
                    .build();
        }
    }
}
