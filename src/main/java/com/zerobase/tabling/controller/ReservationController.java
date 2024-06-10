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
    
    /**
     * 예약 신청
     * 사용자 권한(USER) 필수
     * 같은 매장 상세정보에 중복 예약 불가
     * @param storeDetailId 예약 신청할 매장의 상세정보
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param request
     * Body json
     * {
     *   "headCount": 0 최소 1 이상 입력
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "예약이 신청되었습니다.",
     *     "data": {
     *           "reservationId": 0, 예약 내역 식별번호
     *           "time": "yyyy-MM-ddThh:mm:ss", 예약 시간
     *           "headCount": 0, 예약 인원수
     *           "status": "APPLIED" 예약 상태(신청)
     *         }
     * }
     */
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

    /**
     * 예약 정보 수정
     * 예약 상태가 '신청' 상태일 경우만 수정 가능
     * 매장 상세번호 변경 시 이미 예약한 상세번호로 변경 불가
     * 인원수 변경 시 예약 인원수가 수용 가능 인원을 넘어설 경우 에러 반환
     * @param reservationId 예약 내역 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param request (변경할 항목 외엔 제거)
     * Body json
     * {
     *   "storeDetailId": 0, 매장의 상세정보 식별번호
     *   "headCount": 0 예약 인원수(최소 1)
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "예약 정보 수정이 완료되었습니다.",
     *     "data": null
     * }
     */
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

    /**
     * 일반 사용자가 본인이 예약한 예약 내역 취소
     * 단, 예약 상태가 방문완료인 경우 취소 불가
     * @param reservationId 예약 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "예약이 취소되었습니다.",
     *     "data": null
     * }
     */
    @Operation(summary = "예약 취소")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @PatchMapping("/cancel/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable("reservationId") Long reservationId,
                                               @AuthenticationPrincipal User user) {
        this.reservationService.cancelReservation(reservationId, user.getUserId());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약이 취소되었습니다."));
    }

    /**
     * 일반 사용자가 본인이 예약한 예약 내역 취소
     * 단, 예약 상태가 취소, 혹은 거절인 경우만 삭제 가능
     * @param reservationId 예약 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "예약 내역이 삭제되었습니다.",
     *     "data": null
     * }
     */
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

    /**
     * 사용자의 날짜별 예약(입력 날짜(미입력시 오늘 기준)에 존재하는 예약 시간) 리스트 반환
     * 예약 시간순 정렬 고정
     * @param status 확인할 예약 상태 필터(미입력시 전체)
     * @param date 확인할 날짜('yyyy-MM-dd' 형식)
     * @param page 확인할 페이지
     * @param size 페이지당 목록 개수
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "(입력한 날짜) 날짜의 (입력한 상태) 예약 내역을 가져왔습니다.",
     *     "data": {
     *           "content": [ 결과 리스트
     *               {
     *                   "reservationTime": "string",
     *                   "reservationInfo": [
     *                       {
     *                           "reservationId": 0,
     *                           "store": {
     *                               "storeId": 0,
     *                               "name": "string",
     *                               "location": "string",
     *                               "description": "string"
     *                           },
     *                           "headCount": 0,
     *                           "status": "string"
     *                       }
     *                   ]
     *               }
     *           ],
     *           "pageable": { 페이지 정보
     *               "pageNumber": 0,
     *               "pageSize": 10,
     *               "sort": {
     *                     "empty": true,
     *                     "unsorted": true,
     *                     "sorted": false
     *               },
     *               "offset": 0,
     *               "paged": true,
     *               "unpaged": false
     *           },
     *           "totalElements": 0,
     *           "totalPages": 0,
     *           "last": true,
     *           "size": 0,
     *           "number": 0,
     *           "sort": {
     *               "empty": true,
     *               "unsorted": true,
     *               "sorted": false
     *             },
     *           "first": true,
     *           "numberOfElements": 0,
     *           "empty": false
     *           }
     * }
     */
    @Operation(summary = "사용자의 날짜별 예약 내역 조회")
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
                        date + " 날짜의 " + description + " 예약 내역을 가져왔습니다.",
                        this.reservationService.getUserReservation(status, date, user.getUserId(), pageRequest))
        );
    }

    /**
     * 사용자의 예약 내역 확인
     * @param reservationId 예약 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *   "data": {
     *     "createAt": "yyyy-MM-ddThh:mm:ss", 예약 생성시간 반환
     *     "store": { 매장 정보
     *       "storeId": 0, 매장 식별번호
     *       "name": "string", 매장 이름
     *       "location": "string", 매장 위치
     *       "description": "string" 매장 설명
     *     },
     *     "reservation": { 예약 내역
     *       "reservationId": 0, 예약 내역 식별번호
     *       "time": "yyyy-MM-ddThh:mm:ss", 예약 시간
     *       "headCount": 0, 예약 인원수
     *       "status": "APPROVED" 예약 상태
     *     }
     *   },
     *   "code": "OK",
     *   "message": "예약 내역을 가져왔습니다."
     * }
     */
    @Operation(summary = "예약 내역 상세 조회")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @GetMapping("/detail/{reservationId}")
    public ResponseEntity<?> getReviewDetail(@PathVariable("reservationId") Long reservationId,
                                             @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약 내역을 가져왔습니다.",
                        this.reservationService.getReservationDetail(reservationId, user.getUserId()))
        );
    }

    /**
     * 매장을 등록한 파트너 이용자가 확인하려는 매장의 예약(입력 날짜(미입력시 오늘 기준)에 존재하는 예약 시간) 리스트 조회
     * @param storeId 매장의 식별번호
     * @param status 확인할 예약 상태 필터(미입력시 전체)
     * @param date 확인할 날짜('yyyy-MM-dd' 형식)
     * @param page 확인할 페이지
     * @param size 페이지당 목록 개수
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "(입력한 날짜) 날짜의 매장별 (입력한 상태) 예약 내역을 가져왔습니다.",
     *     "data": {
     *           "content": [
     *               { 결과 리스트
     *                   "reservationTime": "string",
     *                   "reservationInfo": [
     *                       {
     *                           "reservationId": 0,
     *                           "user": {
     *                               "id": 0,
     *                               "name": "string"
     *                           },
     *                           "headCount": 0,
     *                           "status": "APPLIED"
     *                       }
     *                   ]
     *               }
     *           ],
     *           "pageable": { 페이지 정보
     *               "pageNumber": 0,
     *               "pageSize": 0,
     *               "sort": {
     *                   "empty": true,
     *                   "unsorted": true,
     *                   "sorted": false
     *               },
     *               "offset": 0,
     *               "unpaged": false,
     *               "paged": true
     *           },
     *           "last": true,
     *           "totalElements": 0,
     *           "totalPages": 0,
     *           "size": 0,
     *           "number": 0,
     *           "sort": {
     *               "empty": true,
     *               "unsorted": true,
     *               "sorted": false
     *           },
     *           "first": true,
     *           "numberOfElements": 0,
     *           "empty": false
     *         }
     * }
     */
    @Operation(summary = "매장을 등록한 파트너 이용자가 확인하려는 매장의 " +
            "예약(입력 날짜(미입력시 오늘 기준)에 존재하는 예약 시간) 리스트 조회")
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
        if (date == null) date = LocalDate.now();
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        date + " 날짜의 매장별 " + description + " 예약 내역을 가져왔습니다.",
                        this.reservationService.getStoreReservation(storeId, status, date, user.getUserId(), pageRequest))
        );
    }

    /**
     * 매장을 등록한 파트너 이용자가 확인하려는 매장의 전체 예약 신청 리스트 조회
     * @param storeId 매장 식별번호
     * @param page 페이지 번호
     * @param size 페이지당 목록 개수
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *   "data": {
     *           "content": [
     *               { 결과 리스트
     *                   "reservationTime": "string",
     *                   "reservationInfo": [
     *                       {
     *                           "reservationId": 0,
     *                           "user": {
     *                               "id": 0,
     *                               "name": "string"
     *                           },
     *                           "headCount": 0,
     *                           "status": "APPLIED"
     *                       }
     *                   ]
     *               }
     *           ],
     *           "pageable": { 페이지 정보
     *               "pageNumber": 0,
     *               "pageSize": 0,
     *               "sort": {
     *                   "empty": true,
     *                   "unsorted": true,
     *                   "sorted": false
     *               },
     *               "offset": 0,
     *               "unpaged": false,
     *               "paged": true
     *           },
     *           "last": true,
     *           "totalElements": 0,
     *           "totalPages": 0,
     *           "size": 0,
     *           "number": 0,
     *           "sort": {
     *               "empty": true,
     *               "unsorted": true,
     *               "sorted": false
     *           },
     *           "first": true,
     *           "numberOfElements": 0,
     *           "empty": false
     *         },
     *   "code": "OK",
     *   "message": "매장별 예약 신청 목록을 가져왔습니다."
     * }
     */
    @Operation(summary = "매장별 예약신청 내역 조회")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @GetMapping("/store/all-apply/{storeId}")
    public ResponseEntity<?> getAllReviewListByStore(@PathVariable("storeId") Long storeId,
                                                     @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                     @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                     @AuthenticationPrincipal User user) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장별 예약 신청 목록을 가져왔습니다.",
                        this.reservationService.getStoreApplyReservation(storeId, user.getUserId(), pageRequest))
        );
    }

    /**
     * 매장의 파트너 이용자가 일반 이용자로부터 신청된 예약 허가
     * 신청 상태의 예약 내역만 승인 가능
     * @param reservationId 예약 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *   "data": null,
     *   "code": "OK",
     *   "message": "예약을 승인했습니다."
     * }
     */
    @Operation(summary = "예약 승인")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PatchMapping("/approve/{reservationId}")
    public ResponseEntity<?> approveReservation(@PathVariable("reservationId") Long reservationId,
                                                @AuthenticationPrincipal User user) {
        this.reservationService.approveReservation(reservationId, user.getUserId());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약을 승인했습니다."));
    }

    /**
     * 매장의 파트너 이용자가 일반 이용자로부터 신청된 예약 거절
     * 신청 상태의 예약 내역만 거절 가능
     * @param reservationId 예약 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *   "data": null,
     *   "code": "OK",
     *   "message": "예약을 거절했습니다."
     * }
     */
    @Operation(summary = "예약 거절")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PatchMapping("/deny/{reservationId}")
    public ResponseEntity<?> denyReservation(@PathVariable("reservationId") Long reservationId,
                                             @AuthenticationPrincipal User user) {
        this.reservationService.denyReservation(reservationId, user.getUserId());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "예약을 거절했습니다."));
    }

    /**
     * 키오스크에서 방문 완료(예약번호 이용)
     * 권한(로그인) 미필요
     * 승인된 예약 시간의 30분 전 - 10분 전 도착만 방문 완료 가능
     * @param request
     * Body json
     * {
     *     "reservationId": 0 방문완료하려는 예약 번호 입력
     * }
     * @return
     * {
     *   "data": null,
     *   "code": "OK",
     *   "message": "방문 확인 완료되었습니다."
     * }
     */
    @Operation(summary = "키오스크에서 방문 완료(예약번호 이용)")
    @PatchMapping("/kiosk/reservation-id")
    public ResponseEntity<?> kioskByReservationId(
            @Validated @RequestBody ReservationDto.KioskReservationIdRequest request) {
        this.reservationService.kioskByReservationId(request);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "방문 확인 완료되었습니다."));
    }
}
