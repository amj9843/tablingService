package com.zerobase.tabling.exception.impl;

import com.zerobase.tabling.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class ReservationTimeisNotValidException extends AbstractException {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "예약시간 입력 형식이 올바르지 않습니다. 예약 시간 형식 -> 'yyyy-MM-dd HH:mm'";
    }
}
