package com.zerobase.tabling.data.repository.custom.customRepository;

import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomReservationRepository {
    //사용자 아이디로 예약중인 정보 확인
    boolean existsByUserIdAndStoreDetailIdNowReserving(Long userId, Long storeDetailId);

    //현재 시간과 예약 식별번호로 예약 완료 가능한 예약 정보 존재 확인
    boolean existsReservationByReservationIdAndTime(Long reservationId, LocalDateTime now);

    //사용자 식별번호와 예약 식별번호로 예약 상세정보 확인
    Optional<ReservationDto.ReservationDetail> reservationDetailByReservationIdAndUserId(
            Long reservationId, Long userId);

    //입력 일자 기준 사용자 예약 내역 목록 전체 호출
    List<ReservationDto.ListForUser> getUserReservationList(LocalDate date, Long userId);

    //입력 일자 기준 사용자 예약 내역 목록 상태별 호출
    List<ReservationDto.ListForUser> getUserReservationList(ReservationStatus status, LocalDate date, Long userId);

    //입력 일자 기준 매장별 예약 목록 전체 호출
    List<ReservationDto.ListForPartner> getPartnerReservationList(Long storeId, LocalDate date, Long userId);

    //입력 일자 기준 매장별 예약 목록 상태별 호출
    List<ReservationDto.ListForPartner> getPartnerReservationList(Long storeId, ReservationStatus status, LocalDate date, Long userId);

    //매장별 전체 예약 신청 내역 확인(예약 시간 순)
    List<ReservationDto.ListForPartner> getPartnerApplyReservationList(Long storeId, Long userId);
}
