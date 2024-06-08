package com.zerobase.tabling.service;

import com.zerobase.tabling.data.dto.StoreDetailDto;
import com.zerobase.tabling.data.dto.StoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    //매장 전체 목록 조회(이름순, 평점 높은순, 평점 낮은순, 리뷰 많은순, 리뷰 적은순)
    Page<StoreDto.StoreInfo> getStoreList(PageRequest pageRequest);

    //매장 검색(이름순, 평점 높은순, 평점 낮은순, 리뷰 많은순, 리뷰 적은순)
    Page<StoreDto.StoreInfo> getStoreList(PageRequest pageRequest, String word);

    //관리자가 등록한 매장 목록 조회(이름순, 평점 높은순, 평점 낮은순, 리뷰 많은순, 리뷰 적은순)
    Page<StoreDto.StoreInfo> getAllStoreListByPartner(Long userId, PageRequest pageRequest);

    //매장별 예약 가능한 매장 상세 정보 조회(현재 시간 이후 시간 리스트만, 시간순 고정)
    StoreDto.Detail getStoreDetail(Long storeId, Pageable pageable);

    //매장 등록 서비스
    StoreDto.RegistedStoreInfo registStore(Long userId, StoreDto.RegistRequest request);

    //매장 수정 서비스
    void updateStore(Long userId, Long storeId, StoreDto.ModifiedRequest modifiedRequest);

    //매장 삭제 서비스
    void deleteStore(Long userId, Long storeId);

    //매장 상세정보 등록 서비스
    StoreDetailDto.RegistedStoreDetails registStoreDetail(Long userId, Long storeId, List<StoreDetailDto.RegistRequest> requests);

    //매장 상세정보 수정 서비스
    void updateStoreDetail(Long userId, Long storeDetailId, StoreDetailDto.ModifiedRequest request);

    //매장 상세정보 삭제 서비스
    void deleteStoreDetail(Long userId, Long storeDetailId);
}
