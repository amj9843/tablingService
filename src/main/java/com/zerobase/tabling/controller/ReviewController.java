package com.zerobase.tabling.controller;

import com.zerobase.tabling.data.constant.UserRole;
import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.dto.ResultDto;
import com.zerobase.tabling.data.dto.ReviewDto;
import com.zerobase.tabling.service.ReviewService;
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
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    
    //리뷰 작성
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

    //리뷰 수정
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

    //리뷰 삭제
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

    //매장별 리뷰 조회(기본: 최근 등록순)
    @Operation(summary = "매장별 리뷰 조회(기본: 최근 등록순)")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> showStoreReview(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장의 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByStoreLatest(storeId))
        );
    }

    //매장별 리뷰 조회(등록순)
    @Operation(summary = "매장별 리뷰 조회(등록순)")
    @GetMapping("/store/{storeId}/earliest")
    public ResponseEntity<?> showStoreReviewEarliest(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장의 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByStoreEarliest(storeId))
        );
    }

    //매장별 리뷰 조회(별점 높은순/최근 등록순)
    @Operation(summary = "매장별 리뷰 조회(별점 높은순/최근 등록순)")
    @GetMapping("/store/{storeId}/high-rate")
    public ResponseEntity<?> showStoreReviewHighRate(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장의 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByStoreHighRate(storeId))
        );
    }

    //매장별 리뷰 조회(별점 낮은순/최근 등록순)
    @Operation(summary = "매장별 리뷰 조회(별점 낮은순/최근 등록순)")
    @GetMapping("/store/{storeId}/row-rate")
    public ResponseEntity<?> showStoreReviewLowRate(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "매장의 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByStoreLowRate(storeId))
        );
    }

    //유저가 작성한 리뷰 조회(기본: 최근 등록순)
    @Operation(summary = "유저가 쓴 리뷰 조회(기본: 최근 등록순)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> UserReviewListByLatest(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "유저가 작성한 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByUserLatest(userId))
        );
    }

    //유저가 작성한 리뷰 조회
    @Operation(summary = "유저가 쓴 리뷰 조회(등록순)")
    @GetMapping("/user/{userId}/earliest")
    public ResponseEntity<?> UserReviewListByEarliest(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "유저가 작성한 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByUserEarliest(userId))
        );
    }

    //유저가 작성한 리뷰 조회
    @Operation(summary = "유저가 쓴 리뷰 조회(별점 높은순/최근 등록순)")
    @GetMapping("/user/{userId}/high-rate")
    public ResponseEntity<?> UserReviewListByHighRate(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "유저가 작성한 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByUserHighRate(userId))
        );
    }

    //유저가 작성한 리뷰 조회
    @Operation(summary = "유저가 쓴 리뷰 조회(별점 낮은순/최근 등록순)")
    @GetMapping("/user/{userId}/low-rate")
    public ResponseEntity<?> UserReviewListByLowRate(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "유저가 작성한 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByUserLowRate(userId))
        );
    }

    //상세 리뷰 조회
    @Operation(summary = "상세 리뷰 조회")
    @GetMapping("/{reviewId}/detail")
    public ResponseEntity<?> getReviewDetail(@PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "리뷰의 상세 정보를 가져왔습니다.",
                        this.reviewService.getReviewDetail(reviewId))
        );
    }
}
