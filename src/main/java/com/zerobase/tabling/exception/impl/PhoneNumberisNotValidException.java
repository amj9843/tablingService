package com.zerobase.tabling.exception.impl;

import com.zerobase.tabling.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class PhoneNumberisNotValidException extends AbstractException {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "핸드폰 번호 형식이 일치하지 않습니다. " +
                "빈 값을 입력하거나 '010-1234-1234' 형식, " +
                "혹은 '01012341234' 형식을 맞춰주세요.";
    }
}