package com.zerobase.tabling.data.repository.custom.customRepository;

import java.time.LocalDateTime;

public interface CustomReservationRepository {
    //사용자 아이디로 예약중인 정보 확인
    boolean existsByUserIdAndStoreDetailIdNowReserving(Long userId, Long storeDetailId);

    //현재 시간과 예약 식별번호로 예약 완료 가능한 예약 정보 존재 확인
    boolean existsReservationByReservationIdAndTime(Long reservationId, LocalDateTime now);
}
