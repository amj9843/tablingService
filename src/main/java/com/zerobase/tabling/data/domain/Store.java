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

    @Column(name = "user_id")
    //매장 등록자 식별번호
    private Long userId;

    @Column(name = "store_name")
    //매장명
    private String name;

    @Column(name = "location")
    //매장 위치
    private String location;

    @Column(name = "description")
    //매장 설명
    private String description;

    public void update(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }
}
