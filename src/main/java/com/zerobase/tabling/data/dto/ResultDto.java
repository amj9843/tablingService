package com.zerobase.tabling.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ResultDto<T> {
    private HttpStatus code;
    private String message;
    private T data;

    public ResultDto(final HttpStatus code, final String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public static<T> ResultDto<T> res(final HttpStatus code, final String message) {
        return res(code, message, null);
    }

    public static<T> ResultDto<T> res(final HttpStatus code, final String message, final T t) {
        return ResultDto.<T>builder()
                .data(t)
                .code(code)
                .message(message)
                .build();
    }
}
