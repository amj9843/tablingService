package com.zerobase.tabling.service;

import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.dto.ReservationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

public interface ReservationService {
    //매장 예약
    ReservationDto.ReservationInfo applyReservation(Long storeDetailId,
                                                    Long userId, ReservationDto.ApplyRequest request);

    //예약 정보 수정
    void modifyReservation(Long reservationId, Long userId, ReservationDto.ModifyRequest request);

    //예약 취소
    void cancleReservation(Long reservationId, Long userId);

    //사용자의 예약 내역 삭제
    void deleteReservation(Long reservationId, Long userId);

    //사용자의 예약 내역 확인(입력 날짜(미입력시 오늘 기준)에 존재하는 예약 시간 리스트 반환)
    Page<ReservationDto.ListForUser> getUserReservation(
            ReservationStatus status, LocalDate date, Long userId, PageRequest pageRequest);

    //사용자의 예약 상세 확인
    ReservationDto.ReservationDetail getReservationDetail(Long reservationId, Long userId);

    //관리자의 매장별 예약 내역 확인(입력 날짜(미입력시 오늘 기준)에 존재하는 예약 시간 리스트 반환)
    Page<ReservationDto.ListForPartner> getStoreReservation(
            Long storeId, ReservationStatus status, LocalDate date, Long userId, PageRequest pageRequest);

    //관리자의 매장별 전체 예약 신청 내역 확인(예약 시간 순)
    Page<ReservationDto.ListForPartner> getStoreApplyReservation(Long storeId, Long userId, PageRequest pageRequest);

    //관리자의 예약 승인
    void approveReservation(Long reservationId, Long userId);

    //관리자의 예약 거절
    void denyReservation(Long reservationId, Long userId);

    //키오스크에서 방문 완료(예약번호 이용)
    void kioskByReservationId(ReservationDto.KioskReservationIdRequest request);
}
