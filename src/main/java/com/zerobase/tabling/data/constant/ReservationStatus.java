package com.zerobase.tabling.data.constant;

public enum ReservationStatus {
    //신청
    APPLIED("예약 신청 상태"),

    //승인
    APPROVED("예약 승인 상태"),

    //거절
    DENIED("예약 거절 상태"),

    //취소
    CANCELED("예약 취소 상태"),

    //완료
    COMPLETED("방문 완료 상태");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
