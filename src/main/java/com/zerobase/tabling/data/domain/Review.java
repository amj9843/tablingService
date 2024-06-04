package com.zerobase.tabling.data.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    //review 식별번호
    private Long reviewId;

    @Column(name = "reservation_id")
    //연관된 예약 식별번호
    private Long reservationId;

    @Column(name = "rate")
    //평점
    private double rate;

    @Column(name = "context")
    //리뷰 내용
    private String context;
}
