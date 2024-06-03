package com.zerobase.tabling.exception.impl;

import com.zerobase.tabling.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadySignOutException extends AbstractException {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getMessage() {
        return "로그아웃 상태입니다. 다시 로그인 해 주세요.";
    }
}
