package com.zerobase.tabling.service;

import com.zerobase.tabling.data.dto.ReservationDto;

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

    //관리자의 예약 승인
    void approveReservation(Long reservationId, Long userId);

    //관리자의 예약 거절
    void denyReservation(Long reservationId, Long userId);

    //키오스크에서 방문 완료(예약번호 이용)
    void kioskByReservationId(ReservationDto.KioskReservationIdRequest request);
}
