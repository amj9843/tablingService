package com.zerobase.tabling.exception;

import com.zerobase.tabling.data.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<?> handleCustomException(AbstractException e) {
        return new ResponseEntity<>(
                ResultDto.res(e.getStatusCode(), e.getMessage()),
                e.getStatusCode()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HashMap<String, String> errorMessages = new HashMap<>();

        for(FieldError error: e.getFieldErrors()) {
            errorMessages.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(
                ResultDto.res(HttpStatus.BAD_REQUEST, errorMessages.toString()),
                e.getStatusCode()
        );
    }
}
