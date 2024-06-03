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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public static final HttpStatus CODE = HttpStatus.UNAUTHORIZED;
    public static final String MESSAGE = "로그인이 필요합니다.";
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error(MESSAGE, authException);

        String responseBody = objectMapper.writeValueAsString(ResultDto.res(CODE, MESSAGE));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(CODE.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
