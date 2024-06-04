package com.zerobase.tabling.service;

import com.zerobase.tabling.data.dto.StoreDetailDto;
import com.zerobase.tabling.data.dto.StoreDto;

import java.util.List;

public interface StoreService {
    //매장 등록 서비스
    StoreDto.RegistedStoreInfo registStore(Long userId, StoreDto.RegistRequest request);

    //매장 수정 서비스
    //void modifiedStore(Long storeId, StoreDto.ModifiedRequest modifiedRequest);

    //매장 삭제 서비스
    //void deleteStore(Long storeId);

    //매장 상세정보 등록 서비스
    StoreDetailDto.RegistedStoreDetails registStoreDetail(Long userId, Long storeId, List<StoreDetailDto.RegistRequest> requests);

    //매장 상세정보 수정 서비스
    //void modifiedStoreDetail(Long storeDetailId, StoreDto.ModifiedDetailRequest modifiedDetailRequest);

    //매장 상세정보 삭제 서비스
    //void deleteStoreDetail(Long storeDetailId);
}
