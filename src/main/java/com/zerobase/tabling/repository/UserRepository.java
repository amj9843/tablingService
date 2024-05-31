package com.zerobase.tabling.repository;

import com.zerobase.tabling.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //등록되어있는 user 중 입력값과 같은 아이디가 존재하는 지 확인
    boolean existsById(String id);
}
