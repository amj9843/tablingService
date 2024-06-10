package com.zerobase.tabling.controller;

import com.zerobase.tabling.data.constant.ReviewSortType;
import com.zerobase.tabling.data.constant.UserRole;
import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.dto.ResultDto;
import com.zerobase.tabling.data.dto.ReviewDto;
import com.zerobase.tabling.service.ReviewService;
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
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 방문 완료한 이용자가 리뷰 작성
     * 한 예약 내역 당 하나의 리뷰만 작성 가능
     * @param reservationId 방문 완료한 예약 번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param request
     * Body json
     * {
     *     "rate": 0, 1부터 5까지 평점 입력 가능
     *     "context": "string" 리뷰 내용 등록(빈칸 입력 가능)
     * }
     * @return
     * {
     *   "data": {
     *     "reviewedId": 0, 반환된 리뷰 식별번호
     *     "rate": 0, 입력했던 평점
     *     "context": "string" 입력했던 내용
     *   },
     *   "code": "OK",
     *   "message": "리뷰가 작성되었습니다."
     * }
     */
    @Operation(summary = "리뷰 작성")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @PostMapping("/regist/{reservationId}")
    public ResponseEntity<?> registReview(@PathVariable("reservationId") Long reservationId,
                                              @AuthenticationPrincipal User user,
                                              @Validated @RequestBody ReviewDto.RegistRequest request) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "리뷰가 작성되었습니다.",
                        this.reviewService.registReview(reservationId, user.getUserId(), request)));
    }

    /**
     * 리뷰 작성자가 작성했던 리뷰 수정
     * @param reviewId 수정할 리뷰 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param request 변경할 항목 외엔 제거
     * Body json
     * {
     *     "rate": 0, 1부터 5까지 평점 입력 가능
     *     "context": "string" 리뷰 내용 등록(빈칸 입력 가능)
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "리뷰가 수정되었습니다.",
     *     "data": null
     * }
     */
    @Operation(summary = "리뷰 수정")
    @PreAuthorize("hasRole('USER')") //유저 권한만 실행 가능
    @PatchMapping("/update/{reviewId}")
    public ResponseEntity<?> modifyReview(@PathVariable("reviewId") Long reviewId,
                                              @AuthenticationPrincipal User user,
                                              @Validated @RequestBody ReviewDto.ModifiedRequest request) {
        this.reviewService.modifyReview(reviewId, user.getUserId(), request);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "리뷰가 수정되었습니다."));
    }

    /**
     * 리뷰 작성자, 혹은 리뷰가 작성된 매장의 파트너가 삭제할 reviewedId 입력 시 리뷰 삭제
     * @param reviewId 삭제할 리뷰 식별번호
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "리뷰가 삭제되었습니다.",
     *     "data": null
     * }
     */
    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") Long reviewId,
                                              @AuthenticationPrincipal User user) {
        if (user.getRole() == UserRole.PARTNER) {
            //파트너 권한의 유저가 신청했을 경우 > 자신의 매장일 경우 삭제 가능
            this.reviewService.deleteReviewByPartner(reviewId, user.getUserId());
        } else if (user.getRole() == UserRole.USER) {
            //일반 사용자 유저가 신청했을 경우 > 본인이 작성한 리뷰일 경우 삭제 가능
            this.reviewService.deleteReviewByUser(reviewId, user.getUserId());
        }
        
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "리뷰가 삭제되었습니다."));
    }

    /**
     * 매장별 리뷰 조회
     * @param page 보일 페이지
     * @param size 한 페이지당 목록 최대 개수
     * @param criteria 정렬 기준(미입력시 최근 등록순)
     *                 정렬기준:
     *                    - 최근 등록순(CREATEDATDESC)
     *                    - 등록순(CREATEDATASC)
     *                    - 평점 높은순(RATEHIGHEST)
     *                    - 평점 낮은순(RATELOWEST)
     * @param storeId 매장 식별번호
     * @return
     * {
     *     "code": "OK",
     *     "message": "(정렬 기준순)으로 매장의 리뷰 목록을 가져왔습니다.",
     *     "data": {
     *         "content": [ 결과 리스트
     *             {
     *                 "createdAt": "string", 리뷰 작성시간
     *                 "updatedAt": "string", 리뷰 수정시간
     *                 "reviewId": 0, 리뷰 아이디
     *                 "rate": 0, 평점
     *                 "context": "string" 내용
     *             }
     *         ],
     *         "pageable": { 페이지 정보
     *             "pageNumber": 0,
     *             "pageSize": 0,
     *             "sort": {
     *                 "empty": false,
     *                 "sorted": true,
     *                 "unsorted": false
     *             },
     *             "offset": 0,
     *             "unpaged": false,
     *             "paged": true
     *         },
     *         "last": true,
     *         "totalPages": 0,
     *         "totalElements": 0,
     *         "size": 0,
     *         "number": 0,
     *         "sort": {
     *             "empty": false,
     *             "sorted": true,
     *             "unsorted": false
     *         },
     *         "first": true,
     *         "numberOfElements": 0,
     *         "empty": false
     *       }
     * }
     */
    @Operation(summary = "매장별 리뷰 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> showStoreReview(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "criteria", required = false, defaultValue = "CREATEDATDESC") ReviewSortType criteria,
            @PathVariable("storeId") Long storeId) {
        Pageable pageable = PageRequest.of(page, size, criteria.sort());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        criteria.description() + "으로 매장의 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByStore(storeId, pageable))
        );
    }

    /**
     * 유저별 리뷰 조회
     * @param page 보일 페이지
     * @param size 한 페이지당 목록 최대 개수
     * @param criteria 정렬 기준(미입력시 최근 등록순)
     *                 정렬기준:
     *                    - 최근 등록순(CREATEDATDESC)
     *                    - 등록순(CREATEDATASC)
     *                    - 평점 높은순(RATEHIGHEST)
     *                    - 평점 낮은순(RATELOWEST)
     * @param userId 유저 식별번호
     * @return
     * {
     *     "code": "OK",
     *     "message": "(정렬 기준순)으로 유저가 작성한 리뷰 목록을 가져왔습니다.",
     *     "data": {
     *         "content": [ 결과 리스트
     *             {
     *                 "createdAt": "string", 리뷰 작성시간
     *                 "updatedAt": "string", 리뷰 수정시간
     *                 "reviewId": 0, 리뷰 아이디
     *                 "rate": 0, 평점
     *                 "context": "string" 내용
     *             }
     *         ],
     *         "pageable": { 페이지 정보
     *             "pageNumber": 0,
     *             "pageSize": 0,
     *             "sort": {
     *                 "empty": false,
     *                 "sorted": true,
     *                 "unsorted": false
     *             },
     *             "offset": 0,
     *             "unpaged": false,
     *             "paged": true
     *         },
     *         "last": true,
     *         "totalPages": 0,
     *         "totalElements": 0,
     *         "size": 0,
     *         "number": 0,
     *         "sort": {
     *             "empty": false,
     *             "sorted": true,
     *             "unsorted": false
     *         },
     *         "first": true,
     *         "numberOfElements": 0,
     *         "empty": false
     *       }
     * }
     */
    @Operation(summary = "유저가 쓴 리뷰 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> UserReviewListByLatest(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "criteria", required = false, defaultValue = "CREATEDATDESC") ReviewSortType criteria,
            @PathVariable("userId") Long userId) {

        Pageable pageable = PageRequest.of(page, size, criteria.sort());

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        criteria.description()+ "으로 유저가 작성한 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByUser(userId, pageable))
        );
    }

    /**
     * 리뷰 상세내역 확인
     * @param reviewId 상세 내역을 확인하려는 리뷰의 식별번호
     * @return
     * {
     *     "code": "OK",
     *     "message": "리뷰의 상세 정보를 가져왔습니다.",
     *     "data": {
     *       "createAt": "string",
     *       "updateAt": "string",
     *       "reservation": {
     *         "id": 0,
     *         "time": "string",
     *         "headCount": 0
     *       },
     *       "user": {
     *         "id": 0, 유저 식별번호
     *         "name": "string"
     *       },
     *       "rate": 0,
     *       "context": "string"
     *     }
     * }
     */
    @Operation(summary = "상세 리뷰 조회")
    @GetMapping("/detail/{reviewId}")
    public ResponseEntity<?> getReviewDetail(@PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "리뷰의 상세 정보를 가져왔습니다.",
                        this.reviewService.getReviewDetail(reviewId))
        );
    }
}
