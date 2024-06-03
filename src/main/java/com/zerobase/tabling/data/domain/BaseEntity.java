package com.zerobase.tabling.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
// JPA의 엔티티 클래스가 상속받을 경우 자식 클래스에게 매핑 정보를 전달
@MappedSuperclass
// 엔티티를 데이터베이스에 적용하기 전후로 콜백 요청
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    //생성 시간
    private LocalDateTime createdAt;

    @LastModifiedDate
    //마지막 수정 시간
    private LocalDateTime updatedAt;
}
