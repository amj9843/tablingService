package com.zerobase.tabling.controller;

import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.dto.ResultDto;
import com.zerobase.tabling.data.dto.StoreDetailDto;
import com.zerobase.tabling.data.dto.StoreDto;
import com.zerobase.tabling.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;

    //매장 검색(검색 조건별로 단어를 포함하는 매장)

    //매장 상세정보(전체 리뷰, 예약 가능 시간당 인원)

    //매장 전체 목록 조회

    //관리자가 등록한 매장 목록 조회

    //관리자가 매장 등록
    @Operation(summary = "매장 등록")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PostMapping("/regist/store")
    public ResponseEntity<?> registStore(@AuthenticationPrincipal User user,
                                         @Validated @RequestBody StoreDto.RegistRequest requests) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장 등록이 완료되었습니다.",
                        this.storeService.registStore(user.getUserId(), requests)));
    }

    //매장 상세정보(예약가능시간 및 인원수) 등록
    @Operation(summary = "매장 상세정보(예약가능시간 및 인원수) 등록")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PostMapping("/regist/{storeId}/details")
    public ResponseEntity<?> registStoreDetails(@PathVariable("storeId") Long storeId,
                                               @AuthenticationPrincipal User user,
                                               @Validated @RequestBody StoreDetailDto.RegistRequests requests) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장의 상세정보 등록이 완료되었습니다.",
                        this.storeService.registStoreDetail(user.getUserId(), storeId, requests.getStoreDetails())));
    }

    //매장 정보(예약가능시간 및 인원수) 수정
    @Operation(summary = "매장 정보(예약가능시간 및 인원수) 수정")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PatchMapping("/update/{storeId}")
    public ResponseEntity<?> updateStore(@PathVariable Long storeId,
                                           @AuthenticationPrincipal User user,
                                           @Validated @RequestBody StoreDto.ModifiedInfoRequest request) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장 정보 수정이 완료되었습니다."));
    }

    //매장 상세정보(예약가능시간 및 인원수) 수정
    @Operation(summary = "매장 상세정보(예약가능시간 및 인원수) 수정")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PatchMapping("/update/detail/{storeDetailId}")
    public ResponseEntity<?> updateStoreDetail(@PathVariable Long storeDetailId,
                                                 @AuthenticationPrincipal User user,
                                                 @Validated @RequestBody StoreDetailDto.ModifiedInfoRequest request) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장 상세정보 수정이 완료되었습니다."));
    }

    //매장 삭제
    //승인을 기다리는 예약 요청이나 진행중인 예약이 있다면 삭제 불가
    @Operation(summary = "매장 삭제(단, 예약상태가 신청 혹은 승인인 예약이 있을 경우 삭제 불가)")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @DeleteMapping("/delete/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable Long storeId,
                                                 @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장이 삭제되었습니다."));
    }

    //매장 상세정보 삭제
    //관련하여 승인을 기다리는 예약 요청이나 진행중인 예약이 있다면 삭제 불가
    @Operation(summary = "매장 상세정보 삭제(단, 예약상태가 신청 혹은 승인인 예약이 있을 경우 삭제 불가)")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @DeleteMapping("/delete/detail/{storeDetailId}")
    public ResponseEntity<?> deleteStoreDetail(@PathVariable Long storeDetailId,
                                                 @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장 상세정보가 삭제되었습니다."));
    }
}
