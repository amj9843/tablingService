package com.zerobase.tabling.data.domain;

import com.zerobase.tabling.data.constant.ReservationStatus;
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
@Table(name = "reservation")
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    //reservation 식별번호
    private Long reservationId;

    @Column(name = "user_id")
    //예약자의 식별번호
    private Long userId;

    @Column(name = "store_detail_id")
    //매장의 상세정보의 식별번호
    private Long storeDetailId;

    @Column(name = "head_count")
    //예약 인원수
    private int headCount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    //예약 상태
    private ReservationStatus status;

    public void update(Long storeDetailId, int headCount) {
        this.storeDetailId = storeDetailId;
        this.headCount = headCount;
    }

    public void update(ReservationStatus status) {
        this.status = status;
    }
}
