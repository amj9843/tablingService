package com.zerobase.tabling.exception;

import com.zerobase.tabling.data.dto.ResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<?> handleCustomException(AbstractException e) {
        return new ResponseEntity<>(
                ResultDto.res(e.getStatusCode(), e.getMessage()),
                e.getStatusCode()
        );
    }
}
