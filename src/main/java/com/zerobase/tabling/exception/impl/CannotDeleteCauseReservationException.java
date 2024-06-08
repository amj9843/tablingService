package com.zerobase.tabling.exception.impl;

import com.zerobase.tabling.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class CannotDeleteCauseReservationException extends AbstractException {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "요청, 승인 상태의 예약이 있어 삭제/탈퇴가 불가합니다.";
    }
}
