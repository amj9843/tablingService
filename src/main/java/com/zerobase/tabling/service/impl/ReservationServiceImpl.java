package com.zerobase.tabling.service.impl;

import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.Reservation;
import com.zerobase.tabling.data.dto.ReservationDto;
import com.zerobase.tabling.data.dto.StoreDetailDto;
import com.zerobase.tabling.data.repository.ReservationRepository;
import com.zerobase.tabling.data.repository.StoreDetailRepository;
import com.zerobase.tabling.exception.impl.*;
import com.zerobase.tabling.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreDetailRepository storeDetailRepository;

    @Override
    @Transactional
    public ReservationDto.ReservationInfo applyReservation(
            Long storeDetailId, Long userId, ReservationDto.ApplyRequest request) {
        //사용자가 예약하려는 storeDetail 검토
        StoreDetailDto.CustomStoreDetail storeDetail
                = this.validStoreDetail(userId, storeDetailId);

        //예약하려는 타임의 총 인원수가 초과되진 않는지 확인
        if (storeDetail.getHeadCount() < request.getHeadCount()) {
            throw new FullHeadCountException();
        }
        
        //DB에 저장
        Reservation reservation = this.reservationRepository.save(request.toEntity(userId));

        //보여줄 값만 빼서 return
        return new ReservationDto.ReservationInfo().fromEntity(reservation, storeDetail);
    }

    @Override
    @Transactional
    //예약 정보 수정
    public void modifyReservation(Long reservationId, Long userId, ReservationDto.ModifyRequest request) {
        //현재 예약 검토
        Reservation reservation = this.reservationRepository.findByUserIdAndReservationId(userId, reservationId)
                .orElseThrow(NoReservationException::new);
        
        //예약 상태가 신청 상태일 경우만 수정 가능
        if (reservation.getStatus() != ReservationStatus.APPLIED) {
            throw new NoAuthByStatusException();
        }
        
        //인원수를 바꾸려 한다면 새로운 인원 수를 값으로 가져옴
        int headCount = (request.getHeadCount() > 0) ? request.getHeadCount() : reservation.getHeadCount();
        //예약하려는 storeId를 바꾸려 한다면 새로운 storeDetailId를 가져옴
        Long storeDetailId = (request.getStoreDetailId() > 0) ?
                request.getStoreDetailId() : reservation.getReservationId();

        //예약 가능 인원수 확보
        int canReservedHeadCount = reservation.getHeadCount();

        //시간을 바꾸려 한다면 새로운 예약 신청과 동일한 조건 클리어해야함
        if (request.getStoreDetailId() > 0) {
            //사용자가 예약하려는 storeDetail 검토
            StoreDetailDto.CustomStoreDetail storeDetail = this.validStoreDetail(userId, request.getStoreDetailId());
            canReservedHeadCount = storeDetail.getHeadCount();
        } else {
            StoreDetailDto.CustomStoreDetail storeDetail =
                    this.storeDetailRepository.findByStoreDetailIdForReservation(reservation.getStoreDetailId())
                    .orElseThrow(NoStoreDetailException::new);
            canReservedHeadCount += storeDetail.getHeadCount();
        }

        //자신의 예약인원수를 뺀 상태가 추가되어도 현재 문제가 없는 상태인지
        if (headCount > canReservedHeadCount) {
            throw new FullHeadCountException();
        }

        //업데이트
        reservation.update(storeDetailId, headCount);
    }

    @Override
    @Transactional
    //예약 취소
    public void cancleReservation(Long reservationId, Long userId) {
        //사용자가 취소하려는 예약 정보의 상태가 신청 혹은 승인인지 확인
        Reservation reservation = this.reservationRepository.findByUserIdAndReservationId(userId, reservationId)
                .orElseThrow(NoReservationException::new);

        //예약 상태가 신청 혹은 승인 상태일 경우만 수정 가능
        if (reservation.getStatus() != ReservationStatus.APPLIED &&
                reservation.getStatus() != ReservationStatus.APPROVED) {
            throw new NoAuthByStatusException();
        }

        //상태 업데이트
        reservation.update(ReservationStatus.CANCELED);
    }

    @Override
    @Transactional
    //예약 삭제
    public void deleteReservation(Long reservationId, Long userId) {
        //사용자가 삭제하려는 예약 정보 호출
        Reservation reservation = this.reservationRepository.findByUserIdAndReservationId(userId, reservationId)
                .orElseThrow(NoReservationException::new);

        this.reservationRepository.delete(reservation);
    }

    @Override
    @Transactional
    //관리자의 예약 승인
    public void approveReservation(Long reservationId, Long userId) {
        //승인하려는 예약 정보 호출
        Reservation reservation = this.reservationRepository.findByReservationId(reservationId)
                .orElseThrow(NoReservationException::new);

        //자신이 관리하는 매장의 예약 정보인지 권한 확인
        boolean storeOwner
                = this.storeDetailRepository.existsFindByUserIdAndStoreDetailId(userId, reservation.getStoreDetailId());

        //예약상태가 신청 상태가 아니거나 자신이 관리하는 매장의 예약 정보가 아닌 경우 권한 없음 출력
        if (reservation.getStatus() != ReservationStatus.APPLIED
                && !storeOwner) throw new NoAuthException();

        //상태 업데이트
        reservation.update(ReservationStatus.APPROVED);
    }

    @Override
    @Transactional
    //관리자의 예약 거절
    public void denyReservation(Long reservationId, Long userId) {
        //거절하려는 예약 정보 호출
        Reservation reservation = this.reservationRepository.findByReservationId(reservationId)
                .orElseThrow(NoReservationException::new);

        //자신이 관리하는 매장의 예약 정보인지 권한 확인
        boolean storeOwner
                = this.storeDetailRepository.existsFindByUserIdAndStoreDetailId(userId, reservation.getStoreDetailId());

        //예약상태가 신청 상태가 아니거나 자신이 관리하는 매장의 예약 정보가 아닌 경우 권한 없음 출력
        if (reservation.getStatus() != ReservationStatus.APPLIED
                && !storeOwner) throw new NoAuthException();

        //상태 업데이트
        reservation.update(ReservationStatus.DENIED);
    }

    @Override
    @Transactional
    //키오스크에서 방문 완료(예약번호 이용)
    public void kioskByReservationId(ReservationDto.KioskReservationIdRequest request) {
        //방문 완료 처리 가능한 예약 확인(기준 : 예약 시간으로부터 30분 이전 ~ 10분 이전 도착, 예약 승인 상태)
        boolean exists = this.reservationRepository
                .existsReservationByReservationIdAndTime(request.getReservationId(), LocalDateTime.now());

        if (!exists) {
            throw new CannotCompletedReservationException();
        }

        Reservation reservation = this.reservationRepository.findByReservationId(request.getReservationId())
                .orElseThrow(NoReservationException::new);
        
        //상태 업데이트
        reservation.update(ReservationStatus.COMPLETED);
    }

    @Transactional
    public StoreDetailDto.CustomStoreDetail validStoreDetail(Long userId, Long storeDetailId) {
        //사용자가 예약하려는 storeDetailId에 이미 예약했다면 예약 불가(예약 거절, 취소인 경우 제외)
        boolean exists = this.reservationRepository
                .existsByUserIdAndStoreDetailIdNowReserving(userId, storeDetailId);
        if (exists) {
            throw new AlreadyExistReservationException();
        }
        
        //사용자가 예약하려는 storeDetailId의 현재 예약 진행 정도 확인
        return this.storeDetailRepository.findByStoreDetailIdForReservation(storeDetailId)
                        .orElseThrow(NoStoreDetailException::new);
    }
}
