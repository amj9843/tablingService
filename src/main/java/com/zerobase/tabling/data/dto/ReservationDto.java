package com.zerobase.tabling.data.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.Reservation;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

public class ReservationDto {
    //매장 예약 신청 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplyRequest {
        @Min(value = 1, message = "예약 인원수는 최소 1명입니다.")
        private int headCount;

        public Reservation toEntity(Long userId, Long storeDetailId){
            return Reservation.builder()
                    .userId(userId)
                    .storeDetailId(storeDetailId)
                    .headCount(this.headCount)
                    .status(ReservationStatus.APPLIED)
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

        @QueryProjection
        public ReservationInfo(Long reservationId, LocalDateTime time, int headCount, ReservationStatus status) {
            this.reservationId = reservationId;
            this.time = time;
            this.headCount = headCount;
            this.status = status;
        }
    }

    //예약 상세 정보 응답 DTO
    @Data
    @NoArgsConstructor
    public static class ReservationDetail {
        private LocalDateTime createdAt;
        private StoreDto.ForResponse store;
        private ReservationInfo reservation;

        @QueryProjection
        public ReservationDetail(LocalDateTime createdAt, StoreDto.ForResponse store, ReservationInfo reservation) {
            this.createdAt = createdAt;
            this.store = store;
            this.reservation = reservation;
        }
    }

    //리뷰용 예약 정보 응답 DTO
    @Data
    @Builder
    @NoArgsConstructor
    public static class ForResponse {
        private Long id;
        private LocalDateTime time;
        private int headCount;

        @QueryProjection
        public ForResponse(Long reservationId, LocalDateTime time, int headCount) {
            this.id = reservationId;
            this.time = time;
            this.headCount = headCount;
        }
    }

    //예약시간별 리스트 응답 DTO(유저용)
    @Data
    @NoArgsConstructor
    public static class ListForUser {
        private LocalDateTime reservationTime;
        private Set<ForUser> reservationInfo;

        @QueryProjection
        public ListForUser(LocalDateTime reservationTime, Set<ForUser> reservationInfo) {
            this.reservationTime = reservationTime;
            this.reservationInfo = reservationInfo;
        }
    }

    //예약시간별 리스트 응답 DTO(파트너용)
    @Data
    @NoArgsConstructor
    public static class ListForPartner {
        private LocalDateTime reservationTime;
        private Set<ForPartner> reservationInfo;

        @QueryProjection
        public ListForPartner(LocalDateTime reservationTime, Set<ForPartner> reservationInfo) {
            this.reservationTime = reservationTime;
            this.reservationInfo = reservationInfo;
        }
    }

    //파트너의 예약시간별 예약 정보 응답 DTO
    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(of = {"reservationId"})
    public static class ForPartner {
        private Long reservationId;
        private AuthDto.ForResponse user;
        private int headCount;
        private ReservationStatus status;

        @QueryProjection
        public ForPartner(Long reservationId, AuthDto.ForResponse user, int headCount, ReservationStatus status) {
            this.reservationId = reservationId;
            this.user = user;
            this.headCount = headCount;
            this.status = status;
        }
    }

    //유저의 예약시간별 예약 정보 응답 DTO
    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(of = {"reservationId"})
    public static class ForUser {
        private Long reservationId;
        private StoreDto.ForResponse store;
        private int headCount;
        private ReservationStatus status;

        @QueryProjection
        public ForUser(Long reservationId, StoreDto.ForResponse store, int headCount, ReservationStatus status) {
            this.reservationId = reservationId;
            this.store = store;
            this.headCount = headCount;
            this.status = status;
        }
    }
}
