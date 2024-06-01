package com.zerobase.tabling.domain;

import com.zerobase.tabling.domain.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    //reservation 식별번호
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    //사용자와의 다대일 양방향 매핑 : '다'에 해당, user 식별번호(예약자 번호) fk로 가져옴
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_detail_id", referencedColumnName = "store_detail_id")
    //사용자와의 다대일 양방향 매핑 : '다'에 해당, 예약가능시간 fk로 가져옴
    private StoreDetail storeDetail;

    @Column(name = "head_count")
    //예약 인원수
    private int headCount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    //예약 상태
    private ReservationStatus status;

    @OneToOne(mappedBy = "reservation")
    //일대일 양방향 매핑 연관관계 지정 : 주인
    private Review review;
}
