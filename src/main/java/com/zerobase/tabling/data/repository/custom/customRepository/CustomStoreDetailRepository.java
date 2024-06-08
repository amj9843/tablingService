package com.zerobase.tabling.data.repository.custom.customRepository;

import com.zerobase.tabling.data.dto.StoreDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CustomStoreDetailRepository {
    //정보 검토를 위한 예약 정보 따로 담아 호출
    Optional<StoreDetailDto.CustomStoreDetail> findByStoreDetailIdForReservation(Long storeDetailId);
    
    //사용자가 이 예약이 신청된 매장의 관리자인지 매장 상세정보 아이디와 유저 아이디로 확인
    boolean existsFindByUserIdAndStoreDetailId(Long userId, Long storeDetailId);

    //매장 식별번호에 해당하는 매장 상세 정보 호출
    Page<StoreDetailDto.Detail> getDetailsByStoreId(Long storeId, Pageable pageable, LocalDateTime now);
}
