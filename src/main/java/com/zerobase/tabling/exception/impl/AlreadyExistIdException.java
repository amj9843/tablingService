package com.zerobase.tabling.exception.impl;

import com.zerobase.tabling.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistIdException extends AbstractException {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "이미 사용중인 아이디입니다.";
    }
}
