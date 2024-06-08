package com.zerobase.tabling.exception.impl;

import com.zerobase.tabling.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class CannotChangeHeadCountException extends AbstractException {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "예약 가능 인원을 바꾸기에 이미 예약한 사람이 더 많습니다.";
    }
}
