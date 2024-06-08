package com.zerobase.tabling.controller;

import com.zerobase.tabling.data.constant.StoreSortType;
import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.dto.ResultDto;
import com.zerobase.tabling.data.dto.StoreDetailDto;
import com.zerobase.tabling.data.dto.StoreDto;
import com.zerobase.tabling.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    //매장 조회(이름순, 평점 높은순, 평점 낮은순, 리뷰 많은순, 리뷰 적은순), 검색값이 없으면 등록된 전체 매장 반환
    @Operation(summary = "매장 검색")
    @GetMapping("/search/{word}")
    public ResponseEntity<?> getStoreList(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "criteria", required = false, defaultValue = "NAMEASC") StoreSortType criteria,
            @PathVariable(name = "word", required = false) String word
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, criteria.sort());

        if (word == null || word.isBlank()) {
            ResponseEntity.ok(
                    ResultDto.res(HttpStatus.OK,
                            criteria.description() + "으로 등록되어있는 전체 매장 목록을 불러왔습니다.",
                            this.storeService.getStoreList(pageRequest)));
        }

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        criteria.description() + "으로 제목, 위치, 내용에 '"+ word +"'가 포함된 " +
                                "전체 매장 목록을 불러왔습니다.",
                        this.storeService.getStoreList(pageRequest, word)));
    }

    //관리자가 등록한 매장 목록 조회(이름순, 평점 높은순, 평점 낮은순, 리뷰 많은순, 리뷰 적은순)
    @Operation(summary = "파트너 권한인 유저가 자신이 등록한 매장 전체 조회")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @GetMapping("/owner")
    public ResponseEntity<?> getEntryStoreList(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "criteria", required = false, defaultValue = "NAMEASC") StoreSortType criteria
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, criteria.sort());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        criteria.description() + "으로 파트너가 등록한 전체 매장 목록을 불러왔습니다.",
                        this.storeService.getAllStoreListByPartner(user.getUserId(), pageRequest)));
    }

    //매장별 예약 가능한 상세정보
    @Operation(summary = "매장별 예약 가능한 상세 정보 조회")
    @GetMapping("/{storeId}/detail")
    public ResponseEntity<?> getAllStoreList(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @PathVariable("storeId") Long storeId
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장의 상세 정보를 불러왔습니다.",
                        this.storeService.getStoreDetail(storeId, pageable)));
    }

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

    //매장 상세 정보(예약 가능 인원수) 수정
    @Operation(summary = "매장 정보(예약가능시간 및 인원수) 수정")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PatchMapping("/update/{storeId}")
    public ResponseEntity<?> updateStore(@PathVariable("storeId") Long storeId,
                                           @AuthenticationPrincipal User user,
                                           @Validated @RequestBody StoreDto.ModifiedRequest request) {
        this.storeService.updateStore(user.getUserId(), storeId, request);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장 정보 수정이 완료되었습니다."));
    }

    //매장 상세정보(예약가능시간 및 인원수) 수정
    @Operation(summary = "매장 상세정보(예약가능시간 및 인원수) 수정")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PatchMapping("/update/detail/{storeDetailId}")
    public ResponseEntity<?> updateStoreDetail(@PathVariable("storeDetailId") Long storeDetailId,
                                                 @AuthenticationPrincipal User user,
                                                 @Validated @RequestBody StoreDetailDto.ModifiedRequest request) {
        this.storeService.updateStoreDetail(user.getUserId(), storeDetailId, request);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장 상세정보 수정이 완료되었습니다."));
    }

    //매장 삭제
    //승인을 기다리는 예약 요청이나 진행중인 예약이 있다면 삭제 불가
    @Operation(summary = "매장 삭제(단, 예약상태가 신청 혹은 승인인 예약이 있을 경우 삭제 불가)")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @DeleteMapping("/delete/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable("storeId") Long storeId,
                                                 @AuthenticationPrincipal User user) {
        this.storeService.deleteStore(user.getUserId(), storeId);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장이 삭제되었습니다."));
    }

    //매장 상세정보 삭제
    //관련하여 승인을 기다리는 예약 요청이나 진행중인 예약이 있다면 삭제 불가
    @Operation(summary = "매장 상세정보 삭제(단, 예약상태가 신청 혹은 승인인 예약이 있을 경우 삭제 불가)")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @DeleteMapping("/delete/detail/{storeDetailId}")
    public ResponseEntity<?> deleteStoreDetail(@PathVariable("storeDetailId") Long storeDetailId,
                                                 @AuthenticationPrincipal User user) {
        this.storeService.deleteStoreDetail(user.getUserId(), storeDetailId);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장 상세정보가 삭제되었습니다."));
    }
}
