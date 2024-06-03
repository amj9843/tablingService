package com.zerobase.tabling.exception.impl;

import com.zerobase.tabling.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoUserException extends AbstractException {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 유저입니다.";
    }
}
