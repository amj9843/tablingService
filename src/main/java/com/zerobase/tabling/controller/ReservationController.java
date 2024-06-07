package com.zerobase.tabling.controller;

import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.dto.ReservationDto;
import com.zerobase.tabling.data.dto.ResultDto;
import com.zerobase.tabling.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    //매장 예약
    @Operation(summary = "예약 신청")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @PostMapping("/apply/{storeDetailId}")
    public ResponseEntity<?> applyReservation(@PathVariable("storeDetailId") Long storeDetailId,
                                              @AuthenticationPrincipal User user,
                                              @Validated @RequestBody ReservationDto.ApplyRequest request) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약이 신청되었습니다.",
                        this.reservationService.applyReservation(storeDetailId, user.getUserId(), request)));
    }

    //예약 정보 수정(단, 예약 상태가 신청일 때만 수정 가능)
    @Operation(summary = "예약 정보 수정")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @PatchMapping("/update/{reservationId}")
    public ResponseEntity<?> modifyReservation(@PathVariable("reservationId") Long reservationId,
                                               @AuthenticationPrincipal User user,
                                               @Validated @RequestBody ReservationDto.ModifyRequest request) {
        this.reservationService.modifyReservation(reservationId, user.getUserId(), request);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약 정보 수정이 완료되었습니다."));
    }

    //예약 취소(단, 예약 상태가 신청, 승인일 경우만 취소 가능)
    @Operation(summary = "예약 취소")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @PatchMapping("/cancle/{reservationId}")
    public ResponseEntity<?> cancleReservation(@PathVariable("reservationId") Long reservationId,
                                               @AuthenticationPrincipal User user) {
        this.reservationService.cancleReservation(reservationId, user.getUserId());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약이 취소되었습니다."));
    }

    //예약 삭제(단, 예약 상태가 취소, 거절일 경우만 삭제 가능)
    @Operation(summary = "예약 삭제")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @DeleteMapping("/delete/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable("reservationId") Long reservationId,
                                               @AuthenticationPrincipal User user) {
        this.reservationService.deleteReservation(reservationId, user.getUserId());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약 내역이 삭제되었습니다."));
    }

    //사용자의 예약 내역 확인(입력 날짜(미입력시 오늘 기준)에 존재하는 예약 시간 리스트 반환)
    @Operation(summary = "사용자의 예약 내역 조회")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @GetMapping("/list")
    public ResponseEntity<?> getReviewDetail(@RequestParam(name = "status", required = false) ReservationStatus status,
                                             @RequestParam(name = "date", required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                             @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                             @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                             @AuthenticationPrincipal User user) {
        String description = (status == null) ? "전체" : status.description();
        PageRequest pageRequest = PageRequest.of(page, size);
        if (date == null) date = LocalDate.now();
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        date + " 날짜의 " + description + " 리뷰 리스트를 가져왔습니다.",
                        this.reservationService.getUserReservation(status, date, user.getUserId(), pageRequest))
        );
    }

    //사용자의 예약 상세 확인
    @Operation(summary = "예약 내역 상세 조회")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @GetMapping("/{reservationId}/detail")
    public ResponseEntity<?> getReviewDetail(@PathVariable("reservationId") Long reservationId,
                                             @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "리뷰의 상세 정보를 가져왔습니다.",
                        this.reservationService.getReservationDetail(reservationId, user.getUserId()))
        );
    }

    //관리자의 매장별 예약 내역 확인(입력 날짜(미입력시 오늘 기준)에 존재하는 예약 시간 리스트 반환)
    @Operation(summary = "매장별 예약 내역 조회")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getReviewListByStore(@PathVariable("storeId") Long storeId,
                                                  @RequestParam(name = "status", required = false) ReservationStatus status,
                                                  @RequestParam(name = "date", required = false)
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                  @AuthenticationPrincipal User user) {
        String description = (status == null) ? "전체" : status.description();
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        date + " 날짜의 매장별 " + description + " 리뷰 목록을 가져왔습니다.",
                        this.reservationService.getStoreReservation(storeId, status, date, user.getUserId(), pageRequest))
        );
    }

    //관리자의 매장별 전체 예약 신청 내역 확인(예약 시간 순)
    @Operation(summary = "매장별 예약 내역 조회")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @GetMapping("/store/all/{storeId}")
    public ResponseEntity<?> getAllReviewListByStore(@PathVariable("storeId") Long storeId,
                                                     @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                     @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                     @AuthenticationPrincipal User user) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장별 리뷰 목록을 가져왔습니다.",
                        this.reservationService.getStoreApplyReservation(storeId, user.getUserId(), pageRequest))
        );
    }

    //관리자의 예약 승인
    @Operation(summary = "예약 승인")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PatchMapping("/{reservationId}/approve")
    public ResponseEntity<?> approveReservation(@PathVariable("reservationId") Long reservationId,
                                                @AuthenticationPrincipal User user) {
        this.reservationService.denyReservation(reservationId, user.getUserId());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약을 승인했습니다."));
    }

    //관리자의 예약 거절
    @Operation(summary = "예약 거절")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PatchMapping("/{reservationId}/deny")
    public ResponseEntity<?> denyReservation(@PathVariable("reservationId") Long reservationId,
                                             @AuthenticationPrincipal User user) {
        this.reservationService.denyReservation(reservationId, user.getUserId());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약을 거절했습니다."));
    }

    //키오스크에서 방문 완료(예약번호 이용)
    @Operation(summary = "키오스크에서 방문 완료(예약번호 이용)")
    @DeleteMapping("/kiosk/reservation-id")
    public ResponseEntity<?> kioskByReservationId(
            @Validated @RequestBody ReservationDto.KioskReservationIdRequest request) {
        this.reservationService.kioskByReservationId(request);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "방문 확인 완료되었습니다."));
    }
}
