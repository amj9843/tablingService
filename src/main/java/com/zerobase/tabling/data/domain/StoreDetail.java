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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    //매장과의 다대일 단방향 매핑: '다'에 해당, store 식별번호 fk로 가져옴
    private Store store;

    @Column(name = "reservation_time")
    //예약 가능한 시간
    private LocalDateTime reservationTime;

    @Column(name = "head_count")
    //시간별 예약가능한 인원
    private int headCount;
}