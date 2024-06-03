package com.zerobase.tabling.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.tabling.data.dto.ResultDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    public static final HttpStatus CODE = HttpStatus.FORBIDDEN;
    public static final String MESSAGE = "사용 권한이 없습니다.";
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error(MESSAGE, accessDeniedException);

        String responseBody = objectMapper.writeValueAsString(ResultDto.res(CODE, MESSAGE));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(CODE.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
