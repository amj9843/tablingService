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

    /**
     * 매장 전체 조회
     * 권한(로그인) 필요없음
     * @param page 보일 페이지
     * @param size 한 페이지당 목록 최대 개수
     * @param criteria 정렬 기준(미입력시 이름순)
     *                 정렬기준:
     *                    - 이름순(NAMEASC)
     *                    - 평점 높은순(RATEHIGHEST)
     *                    - 평점 낮은순(RATELOWEST)
     *                    - 리뷰 많은순(REVIEWCOUNTHIGHEST)
     *                    - 리뷰 적은순(REVIEWCOUNTLOWEST)
     * @return
     * {
     *     "code": "OK",
     *     "message": "(정렬 기준순)으로 등록되어있는 전체 매장 목록을 불러왔습니다.",
     *     "data": {
     *           "content": [ 결과 리스트
     *             {
     *                 "storeId": 0,
     *                 "name": "string",
     *                 "location": "string",
     *                 "description": "",
     *                 "rate": 0.0,
     *                 "reviewCount": 0
     *             }
     *           ],
     *           "pageable": { 페이지 정보
     *             "pageNumber": 0,
     *             "pageSize": 0,
     *             "sort": {
     *               "empty": false,
     *               "unsorted": false,
     *               "sorted": true
     *             },
     *             "offset": 0,
     *             "paged": true,
     *             "unpaged": false
     *           },
     *           "last": true,
     *           "totalPages": 0,
     *           "totalElements": 0,
     *           "size": 0,
     *           "number": 0,
     *           "sort": {
     *             "empty": false,
     *             "unsorted": false,
     *             "sorted": true
     *           },
     *           "first": true,
     *           "numberOfElements": 0,
     *           "empty": false
     *         }
     * }
     */
    @Operation(summary = "매장 전체 조회")
    @GetMapping("/search")
    public ResponseEntity<?> getStoreAllList(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "criteria", required = false, defaultValue = "NAMEASC") StoreSortType criteria
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        criteria.description() + "으로 등록되어있는 전체 매장 목록을 불러왔습니다.",
                        this.storeService.getStoreList(pageRequest, criteria.comparator())));
    }

    /**
     * 매장 검색
     * 권한(로그인) 필요없음
     * @param page 보일 페이지
     * @param size 한 페이지당 목록 최대 개수
     * @param criteria 정렬 기준(미입력시 이름순)
     *                 정렬기준:
     *                    - 이름순(NAMEASC)
     *                    - 평점 높은순(RATEHIGHEST)
     *                    - 평점 낮은순(RATELOWEST)
     *                    - 리뷰 많은순(REVIEWCOUNTHIGHEST)
     *                    - 리뷰 적은순(REVIEWCOUNTLOWEST)
     * @param word 검색 단어(없을 시 전체 매장 조회)
     * @return
     * {
     *     "code": "OK",
     *     "message": "(정렬 기준순)으로 제목, 위치, 내용에 (검색어)가 포함된 전체 매장 목록을 불러왔습니다.",
     *     "data": {
     *           "content": [ 결과 리스트
     *             {
     *                 "storeId": 0,
     *                 "name": "string",
     *                 "location": "string",
     *                 "description": "",
     *                 "rate": 0.0,
     *                 "reviewCount": 0
     *             }
     *           ],
     *           "pageable": { 페이지 정보
     *             "pageNumber": 0,
     *             "pageSize": 0,
     *             "sort": {
     *               "empty": false,
     *               "unsorted": false,
     *               "sorted": true
     *             },
     *             "offset": 0,
     *             "paged": true,
     *             "unpaged": false
     *           },
     *           "last": true,
     *           "totalPages": 0,
     *           "totalElements": 0,
     *           "size": 0,
     *           "number": 0,
     *           "sort": {
     *             "empty": false,
     *             "unsorted": false,
     *             "sorted": true
     *           },
     *           "first": true,
     *           "numberOfElements": 0,
     *           "empty": false
     *         }
     * }
     */
    @Operation(summary = "매장 검색")
    @GetMapping("/search/{word}")
    public ResponseEntity<?> getStoreList(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "criteria", required = false, defaultValue = "NAMEASC") StoreSortType criteria,
            @PathVariable(name = "word", required = false) String word
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        criteria.description() + "으로 제목, 위치, 내용에 '"+ word +"'가 포함된 " +
                                "전체 매장 목록을 불러왔습니다.",
                        this.storeService.getStoreList(pageRequest, word, criteria.comparator())));
    }

    /**
     * 관리자가 등록한 매장 목록 조회
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param page 보일 페이지
     * @param size 한 페이지당 목록 최대 개수
     * @param criteria 정렬 기준(미입력시 이름순)
     *                 정렬기준:
     *                    - 이름순(NAMEASC)
     *                    - 평점 높은순(RATEHIGHEST)
     *                    - 평점 낮은순(RATELOWEST)
     *                    - 리뷰 많은순(REVIEWCOUNTHIGHEST)
     *                    - 리뷰 적은순(REVIEWCOUNTLOWEST)
     * @return
     * {
     *     "code": "OK",
     *     "message": "(정렬 기준순)으로 파트너가 등록한 전체 매장 목록을 불러왔습니다.",
     *     "data": {
     *           "content": [ 결과 리스트
     *             {
     *                 "storeId": 0,
     *                 "name": "string",
     *                 "location": "string",
     *                 "description": "",
     *                 "rate": 0.0,
     *                 "reviewCount": 0
     *             }
     *           ],
     *           "pageable": { 페이지 정보
     *             "pageNumber": 0,
     *             "pageSize": 0,
     *             "sort": {
     *               "empty": false,
     *               "unsorted": false,
     *               "sorted": true
     *             },
     *             "offset": 0,
     *             "paged": true,
     *             "unpaged": false
     *           },
     *           "last": true,
     *           "totalPages": 0,
     *           "totalElements": 0,
     *           "size": 0,
     *           "number": 0,
     *           "sort": {
     *             "empty": false,
     *             "unsorted": false,
     *             "sorted": true
     *           },
     *           "first": true,
     *           "numberOfElements": 0,
     *           "empty": false
     *         }
     * }
     */
    @Operation(summary = "파트너 권한인 유저가 자신이 등록한 매장 전체 조회")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @GetMapping("/my-store")
    public ResponseEntity<?> getEntryStoreList(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "criteria", required = false, defaultValue = "NAMEASC") StoreSortType criteria
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        criteria.description() + "으로 파트너가 등록한 전체 매장 목록을 불러왔습니다.",
                        this.storeService.getAllStoreListByPartner(user.getUserId(), pageRequest, criteria.comparator())));
    }

    /**
     * 매장별 예약 가능한 상세정보 조회
     * 현재 시간 이후의 예약 가능한 시간대를 예약 시간이 빠른 순의 리스트로 가져옴
     * 권한(로그인) 필요 없음
     * @param page 보일 페이지
     * @param size 한 페이지당 목록 최대 개수
     * @param storeId 매장 상세를 확인하고싶은 매장의 식별번호
     * @return
     * {
     *     "code": "OK",
     *     "message": "매장의 상세 정보를 불러왔습니다.",
     *     "data": {
     *         "data": {
     *         "storeId": 0,
     *         "name": "string",
     *         "location": "string",
     *         "description": "string",
     *         "rate": 0.0, 매장 평점
     *         "reviewCount": 0, 매장 리뷰 수
     *         "details": { 예약 가능 시간별 매장의 상세정보
     *             "content": [
     *                 {
     *                     "storeDetailId": 0,
     *                     "reservationTime": "string",
     *                     "totalHeadCount": 0, 총 예약 가능 인원
     *                     "nowHeadCount": 0 현재까지 예약한 인원
     *                 }
     *             ],
     *             "pageable": {
     *                 "pageNumber": 0,
     *                 "pageSize": 10,
     *                 "sort": {
     *                     "empty": true,
     *                     "sorted": false,
     *                     "unsorted": true
     *                 },
     *                 "offset": 0,
     *                 "unpaged": false,
     *                 "paged": true
     *             },
     *             "last": true,
     *             "totalPages": 1,
     *             "totalElements": 2,
     *             "size": 10,
     *             "number": 0,
     *             "sort": {
     *                 "empty": true,
     *                 "sorted": false,
     *                 "unsorted": true
     *             },
     *             "first": true,
     *             "numberOfElements": 2,
     *             "empty": false
     *             }
     *         },
     *         "code": "string",
     *         "message": "string"
     *       }
     * }
     */
    @Operation(summary = "매장별 예약 가능한 상세 정보 조회")
    @GetMapping("/detail/{storeId}")
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

    /**
     * PARTNER 권한을 가진 사용자가 매장 등록
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param requests
     * Body json
     * {
     *   "name": "string",
     *   "location": "string",
     *   "description": "string"
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "매장 등록이 완료되었습니다.",
     *     "data": {
     *         "ownerId": 0, 등록한 파트너의 유저 식별번호(userId)
     *         "storeId": 0, 등록된 매장의 식별번호
     *         "name": "string", 등록한 매장의 이름
     *         "location": "location", 등록한 매장의 위치
     *         "description": "description" 등록한 매장의 설명
     *     }
     * }
     */
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

    /**
     * PARTNER 권한을 가진 사용자가 매장 상세정보(예약가능시간 및 인원수) 등록
     * 로그인한 이용자가 등록한 매장의 상세정보가 아닌 경우 등록 불가
     * @param storeId 상세정보를 등록할 매장의 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param requests
     * Body json
     * {
     *     "storeDetails": [
     *           {
     *             "reservationTime": "string", 등록하려는 예약 가능한 시간('yyyy-MM-dd hh:mm' 형식)
     *             "headCount": 0 해당 시간에 예약 가능한 총 인원 수
     *           }
     *         ]
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "매장의 상세정보 등록이 완료되었습니다.",
     *     "data": {
     *         "storeId": 0,
     *         "registedStoreDetails": [
     *           {
     *             "storeDetailId": 0,
     *             "reservationTime": "string",
     *             "headCount": 0
     *           }
     *         ]
     *     }
     * }
     */
    @Operation(summary = "매장 상세정보(예약가능시간 및 인원수) 등록")
    @PreAuthorize("hasRole('PARTNER')") //파트너 권한만 실행 가능
    @PostMapping("/regist/details/{storeId}")
    public ResponseEntity<?> registStoreDetails(@PathVariable("storeId") Long storeId,
                                               @AuthenticationPrincipal User user,
                                               @Validated @RequestBody StoreDetailDto.RegistRequests requests) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장의 상세정보 등록이 완료되었습니다.",
                        this.storeService.registStoreDetail(user.getUserId(), storeId, requests.getStoreDetails())));
    }

    /**
     * PARTNER 권한을 가진 사용자가 매장 정보 수정
     * 로그인한 이용자가 등록한 매장이 아닌 경우 수정 불가
     * @param storeId 매장을 등록할 때 받은 매장 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param request (변경할 항목 외엔 제거)
     * Body json
     * {
     *     "name": "string", 매장 이름
     *     "location": "string", 매장 위치
     *     "description": "string" 매장 설명
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "매장 정보 수정이 완료되었습니다.",
     *     "data": null
     * }
     */
    @Operation(summary = "매장 정보(매장명, 위치, 설명) 수정")
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

    /**
     * PARTNER 권한을 가진 사용자가 매장 상세정보의 예약 가능 인원수 수정
     * 로그인한 이용자가 등록한 매장 관련 상세 정보가 아닌 경우 수정 불가
     * @param storeDetailId 예약 상세정보 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param request
     * Body json
     * {
     *     "headCount": 0 예약 가능 총 인원 수(최소 1 이상 입력)
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "매장 상세정보 수정이 완료되었습니다.",
     *     "data": null
     * }
     */
    @Operation(summary = "매장 상세정보의 예약 가능 인원수 수정")
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

    /**
     * 매장 삭제
     * 단, 해당 매장에 승인을 기다리는 예약 요청이나 진행중인 예약이 있다면 매장 삭제 불가
     * 로그인한 이용자가 등록한 매장이 아닌 경우 삭제 불가
     * @param storeId 매장의 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "매장이 삭제되었습니다.",
     *     "data": null
     * }
     */
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

    /**
     * 매장 상세정보 삭제
     * 단, 해당 상세정보와 관련하여 승인을 기다리는 예약 요청이나 진행중인 예약이 있다면 삭제 불가
     * 로그인한 유저가 관리하는 매장의 상세정보가 아닌 경우 삭제 불가
     * @param storeDetailId 매장 상세정보 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "매장 상세정보가 삭제되었습니다.",
     *     "data": null
     * }
     */
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
