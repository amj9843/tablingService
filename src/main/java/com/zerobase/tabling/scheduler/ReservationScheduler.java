package com.zerobase.tabling.scheduler;

import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.Reservation;
import com.zerobase.tabling.data.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Transactional
@AllArgsConstructor
public class ReservationScheduler {
    private final ReservationRepository reservationRepository;

    /**
     * 매일 자정마다 전날까지의 예약정보 중 시간 내에 방문완료되지 못한 예약내역들 예약 취소
     */
    @Scheduled(cron = "${scheduler.reservation.delete-no-show}")
    public void canceledNoShow() {
        //매장 상세정보와 예약 정보를 조회해 방문해야했던 시간이 하루 전인데 예약 상태가 APPLIED(신청), APPROVED(승인) 상태인 항목들 조회
        List<Long> reservations = this.reservationRepository.findAllNoShowReservations();

        try {
            //예약 식별번호마다 예약 취소(상태 업데이트)
            for (Long reservationId: reservations) {
                Optional<Reservation> reservation = this.reservationRepository.findByReservationId(reservationId);
                reservation.ifPresent(r -> r.update(ReservationStatus.CANCELED));
            }

            log.info("completed to canceled no-show reservation information. reservationIds -> " + reservations.toString());
        } catch (Exception e) {
            log.error("failed to canceled no-show reservation information. {}", e.getMessage());
        }
    }
}
