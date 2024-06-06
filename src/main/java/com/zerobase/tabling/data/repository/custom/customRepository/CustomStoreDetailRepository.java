package com.zerobase.tabling.data.repository.custom.customRepository;

import com.zerobase.tabling.data.dto.StoreDetailDto;

import java.util.Optional;

public interface CustomStoreDetailRepository {
    Optional<StoreDetailDto.CustomStoreDetail> findByStoreDetailIdForReservation(Long storeDetailId);
    
    //사용자가 이 예약이 신청된 매장의 관리자인지 매장 상세정보 아이디와 유저 아이디로 확인
    boolean existsFindByUserIdAndStoreDetailId(Long userId, Long storeDetailId);
}
