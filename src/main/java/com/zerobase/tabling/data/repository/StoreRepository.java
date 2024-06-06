package com.zerobase.tabling.data.repository;

import com.zerobase.tabling.data.domain.Store;
import com.zerobase.tabling.data.repository.custom.customRepository.CustomStoreRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, CustomStoreRepository {
    //등록되어있는 매장 중 매장 관리인, 매장명, 위치가 동일한 매장이 있는지 확인
    boolean existsByUserIdAndNameAndLocation(Long userId, String name, String location);

    //매장아이디로 매장 호출
    Optional<Store> findByStoreId(Long storeId);
}
