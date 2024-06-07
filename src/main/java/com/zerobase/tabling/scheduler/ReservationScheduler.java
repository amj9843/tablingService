package com.zerobase.tabling.scheduler;

import com.zerobase.tabling.data.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class ReservationScheduler {
    private final ReservationRepository reservationRepository;

    /**
     * 매일 자정마다 전날까지의 예약정보 중 시간 내에 방문완료되지 못한 예약내역들 예약 취소
     */
    @Scheduled(cron = "${scheduler.reservation.delete-no-show}")
    public void cancledNoShow() {
        try {
            log.info("completed to deleting no-show reservation information.");
        } catch (Exception e) {
            log.error("failed to deleting no-show reservation information. {}", e.getMessage());
        }
    }
}
