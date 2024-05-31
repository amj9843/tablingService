package com.zerobase.tabling.repository;

import com.zerobase.tabling.domain.StoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreDetailRepository extends JpaRepository<StoreDetail, Long> {
}
