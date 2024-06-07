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

    //매장별 리뷰 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
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

    //유저가 작성한 리뷰 조회(기준: 최근 등록순, 등록순, 평점 높은순, 평점 낮은순)
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
                        "유저가 작성한 리뷰 목록을 가져왔습니다.",
                        this.reviewService.getReviewListByUser(userId, pageable))
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