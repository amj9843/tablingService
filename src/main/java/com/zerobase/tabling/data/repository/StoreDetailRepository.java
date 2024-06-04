package com.zerobase.tabling.data.repository;

import com.zerobase.tabling.data.domain.Store;
import com.zerobase.tabling.data.domain.StoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface StoreDetailRepository extends JpaRepository<StoreDetail, Long> {
    boolean existsByStoreAndReservationTime(Store store, LocalDateTime reservationTime);
}
