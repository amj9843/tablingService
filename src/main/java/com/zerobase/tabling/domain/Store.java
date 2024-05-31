package com.zerobase.tabling.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
    //사용자와의 다대일 양방향 매핑 : '다'에 해당, user 식별번호 fk로 가져옴
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

    @OneToMany(mappedBy = "store")
    //예약가능정보와의 다대일 양방향 매핑 연관관계 지정: '일'에 해당
    private List<StoreDetail> storeDetails = new ArrayList<>();
}
