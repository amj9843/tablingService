package com.zerobase.tabling.data.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store_detail")
public class StoreDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_detail_id")
    //매장 상세 정보 식별 번호
    private Long storeDetailId;

    @Column(name = "store_Id")
    //연관된 매장의 식별 번호
    private Long storeId;

    @Column(name = "reservation_time")
    //예약 가능한 시간
    private LocalDateTime reservationTime;

    @Column(name = "head_count")
    //시간별 예약가능한 인원
    private int headCount;
}