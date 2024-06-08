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

    //매장 식별번호로 진행중인 예약이 있는지 확인
    boolean existsProgressReservationByStoreId(Long storeId);

    //매장 상세정보 식별번호로 진행중인 예약이 있는지 확인
    boolean existsProgressReservationByStoreDetailId(Long storeDetailId);

    //유저 식별번호로 관리중인 매장에서 진행중인 예약이 있는지 확인(파트너)
    boolean existsProgressReservationByPartnerUserId(Long userId);

    //유저 식별번호로 진행중인 예약이 있는지 확인(일반 이용자)
    boolean existsProgressReservationByUserUserId(Long userId);

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

    //매장 상세 정보 관련해 예약한 총 인원 수
    int countHeadCountByStoreDetailId(Long storeDetailId);
}
