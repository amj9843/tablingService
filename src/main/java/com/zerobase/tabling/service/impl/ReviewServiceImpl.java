package com.zerobase.tabling.service.impl;

import com.zerobase.tabling.data.constant.ReservationStatus;
import com.zerobase.tabling.data.domain.Reservation;
import com.zerobase.tabling.data.domain.Review;
import com.zerobase.tabling.data.dto.ReviewDto;
import com.zerobase.tabling.data.repository.ReservationRepository;
import com.zerobase.tabling.data.repository.ReviewRepository;
import com.zerobase.tabling.data.repository.StoreRepository;
import com.zerobase.tabling.exception.impl.AlreadyExistReviewException;
import com.zerobase.tabling.exception.impl.NoAuthByStatusException;
import com.zerobase.tabling.exception.impl.NoAuthException;
import com.zerobase.tabling.exception.impl.NoReviewException;
import com.zerobase.tabling.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    //리뷰 등록
    public ReviewDto.RegistResponse registReview(Long reservationId, Long userId, ReviewDto.RegistRequest request) {
        //이미 작성한 예약이 있는지 확인
        boolean exists = this.reviewRepository.existsByReservationId(reservationId);
        if (exists) {
            throw new AlreadyExistReviewException();
        }

        //사용자가 작성하려는 리뷰가 방문완료가 된 예약의 리뷰인지 확인
        Reservation reservation =
                this.reservationRepository.findByReservationIdAndStatus(reservationId, ReservationStatus.COMPLETED)
                        .orElseThrow(NoAuthByStatusException::new);

        //리뷰 내용이 아예 null -> 빈 값 등록
        if (request.getContext() == null) request.setContext("");

        //DB에 저장
        Review review = this.reviewRepository.save(request.toEntity(reservationId));

        //보여줄 값만 빼서 return
        return new ReviewDto.RegistResponse(review.getReviewId(), review.getRate(), review.getContext());
    }

    @Override
    @Transactional
    //리뷰 수정
    public void modifyReview(Long reviewId, Long userId, ReviewDto.ModifiedRequest request) {
        //수정하려는 리뷰 호출
        Review review = this.reviewRepository.findByReviewId(reviewId).orElseThrow(NoReviewException::new);

        //리뷰를 작성한 유저(예약한 유저)가 맞는지 확인
        boolean exists = this.reservationRepository.existsByReservationIdAndUserId(review.getReservationId(), userId);
        if (!exists) {
            throw new NoAuthException();
        }
        
        //평점, 내용에 입력값이 있다면 해당 입력값으로, 아니면 원래 입력값으로 고정
        int rate = (request.getRate() > 0) ? request.getRate() : review.getRate();
        String context = (request.getContext() != null) ? request.getContext() : review.getContext();
    
        //값 업데이트
        review.update(rate, context);
    }

    @Override
    @Transactional
    //리뷰 삭제(유저)
    public void deleteReviewByUser(Long reviewId, Long userId) {
        //삭제하려는 리뷰 호출
        Review review = this.reviewRepository.findByReviewId(reviewId).orElseThrow(NoReviewException::new);

        //리뷰를 작성한 유저(예약한 유저)가 맞는지 확인
        boolean exists = this.reservationRepository.existsByReservationIdAndUserId(review.getReservationId(), userId);
        if (!exists) {
            throw new NoAuthException();
        }

        this.reviewRepository.delete(review);
    }

    @Override
    @Transactional
    //리뷰 삭제(파트너)
    public void deleteReviewByPartner(Long reviewId, Long userId) {
        //삭제하려는 리뷰 호출
        Review review = this.reviewRepository.findByReviewId(reviewId).orElseThrow(NoReviewException::new);
        
        //리뷰가 작성된 매장의 관리자인지 확인
        boolean exists = this.storeRepository.existsByReservationIdAndUserId(review.getReservationId(), userId);
        if (!exists) {
            throw new NoAuthException();
        }

        this.reviewRepository.delete(review);
    }

    @Override
    //매장별 리뷰 조회(최근 등록순)
    public List<ReviewDto.StoreReviewInfo> getReviewListByStoreLatest(Long storeId) {
        return this.reviewRepository.reviewListByStoreIdOrderByLatest(storeId);
    }

    @Override
    //매장별 리뷰 조회(등록순)
    public List<ReviewDto.StoreReviewInfo> getReviewListByStoreEarliest(Long storeId) {
        return this.reviewRepository.reviewListByStoreIdOrderByEarliest(storeId);
    }

    @Override
    //매장별 리뷰 조회(평점 높은순/최근 등록순)
    public List<ReviewDto.StoreReviewInfo> getReviewListByStoreHighRate(Long storeId) {
        return this.reviewRepository.reviewListByStoreIdOrderByHighRate(storeId);
    }

    @Override
    //매장별 리뷰 조회(평점 낮은순/등록순)
    public List<ReviewDto.StoreReviewInfo> getReviewListByStoreLowRate(Long storeId) {
        return this.reviewRepository.reviewListByStoreIdOrderByLowRate(storeId);
    }

    @Override
    //유저별 리뷰 조회(최근 등록순)
    public List<ReviewDto.UserReviewInfo> getReviewListByUserLatest(Long userId) {
        return this.reviewRepository.reviewListByUserIdOrderByLatest(userId);
    }

    @Override
    //유저별 리뷰 조회(등록순)
    public List<ReviewDto.UserReviewInfo> getReviewListByUserEarliest(Long userId) {
        return this.reviewRepository.reviewListByUserIdOrderByEarliest(userId);
    }

    @Override
    //유저별 리뷰 조회(평점 높은순/최근 등록순)
    public List<ReviewDto.UserReviewInfo> getReviewListByUserHighRate(Long userId) {
        return this.reviewRepository.reviewListByUserIdOrderByHighRate(userId);
    }

    @Override
    //유저별 리뷰 조회(평점 낮은순/최근 등록순)
    public List<ReviewDto.UserReviewInfo> getReviewListByUserLowRate(Long userId) {
        return this.reviewRepository.reviewListByUserIdOrderByLowRate(userId);
    }

    @Override
    //리뷰 상세정보 조회(예약 정보, 등록자 이름)
    public ReviewDto.ReviewDetail getReviewDetail(Long reviewId) {
        return this.reviewRepository.findReviewDetailByReviewId(reviewId)
                .orElseThrow(NoReviewException::new);
    }
}
