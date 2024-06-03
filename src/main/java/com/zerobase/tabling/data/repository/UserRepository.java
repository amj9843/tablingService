package com.zerobase.tabling.data.repository;

import com.zerobase.tabling.data.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //등록되어있는 user 중 아이디가 일치하는 user 검색
    Optional<User> findById(String id);

    //등록되어있는 user 중 입력값과 같은 아이디가 존재하는 지 확인
    boolean existsById(String id);

    //등록되어있는 user 중 식별번호가 일치하는 user 검색
    Optional<User> findByUserId(Long id);

    //userId로 user 삭제
    @Transactional
    void deleteByUserId(Long userId);
}
