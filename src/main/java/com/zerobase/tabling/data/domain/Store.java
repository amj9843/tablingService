package com.zerobase.tabling.data.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    //store 식별번호
    private Long storeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    //사용자와의 다대일 단방향 매핑 : '다'에 해당, user 식별번호 fk로 가져옴
    private User user;

    @Column(name = "store_name")
    //매장명
    private String name;

    @Column(name = "location")
    //매장 위치
    private String location;

    @Column(name = "description")
    //매장 설명
    private String description;
}
