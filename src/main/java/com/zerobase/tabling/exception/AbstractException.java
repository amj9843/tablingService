package com.zerobase.tabling.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends RuntimeException{
    abstract public HttpStatus getStatusCode();
    abstract public String getMessage();
}