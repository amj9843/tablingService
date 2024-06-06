package com.zerobase.tabling.data.repository;

import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.Reservation;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, CustomReservationRepository {
    //유저 식별번호, 예약 식별번호로 예약 정보 호출
    Optional<Reservation> findByUserIdAndReservationId(Long userId, Long reservationId);

    //예약 정보 식별번호로 예약 정보 호출
    Optional<Reservation> findByReservationId(Long reservationId);
    
    //예약 정보 식별번호와 예약 상태로 예약 정보 호출
    Optional<Reservation>findByReservationIdAndStatus(Long reservationId, ReservationStatus status);

    //예약 식별벌호와 유저아이디로 일치하는 예약 정보가 있는지 확인
    boolean existsByReservationIdAndUserId(Long reservationId, Long userId);
}
