package com.zerobase.tabling.data.repository;

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
}
