package com.zerobase.tabling.data.repository.custom.customRepository;

import com.zerobase.tabling.data.dto.StoreDto;

import java.util.List;
import java.util.Optional;

public interface CustomStoreRepository {
    //사용자가 이 예약이 신청된 매장의 관리자인지 매장 상세정보 아이디와 유저 아이디로 확인
    boolean existsByReservationIdAndUserId(Long reservationId, Long userId);

    //매장 전체 목록 호출
    List<StoreDto.StoreInfo> getStoreList();

    //검색한 내용이 포함된 매장 목록 호출
    List<StoreDto.StoreInfo> getStoreList(String word);

    //파트너가 관리하는 매장 목록 자신의 유저 식별번호로 호출
    List<StoreDto.StoreInfo> getStoreListByPartner(Long userId);

    //매장 식별번호에 해당하는 매장 정보 StoreInfo에 담아 호출
    Optional<StoreDto.StoreInfo> getStoreInfoByStoreId(Long storeId);
}
