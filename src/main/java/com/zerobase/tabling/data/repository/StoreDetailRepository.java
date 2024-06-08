package com.zerobase.tabling.data.repository;

import com.zerobase.tabling.data.domain.StoreDetail;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomStoreDetailRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface StoreDetailRepository extends JpaRepository<StoreDetail, Long>, CustomStoreDetailRepository {
    boolean existsByStoreIdAndReservationTime(Long storeId, LocalDateTime reservationTime);

    //등록되어있는 storeDetail 중 식별번호가 일치하는 storeDetail 검색
    Optional<StoreDetail> findByStoreDetailId(Long storeDetailId);

    @Transactional
    void deleteByStoreDetailId(Long StoreDetailId);
}
