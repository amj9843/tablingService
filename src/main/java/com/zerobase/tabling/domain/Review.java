package com.zerobase.tabling.domain;

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

    @OneToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id")
    //일대일 양방향 매핑 연관관계 지정, reservation 식별번호를 fk로 가져옴
    private Reservation reservation;

    @Column(name = "rate")
    //평점
    private double rate;

    @Column(name = "context")
    //리뷰 내용
    private String context;
}
