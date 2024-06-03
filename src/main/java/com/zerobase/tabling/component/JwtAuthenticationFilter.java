package com.zerobase.tabling.component;

import com.zerobase.tabling.exception.impl.AlreadySignOutException;
import com.zerobase.tabling.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final RedisComponent redis;

    @Override
    //JWT 토큰 검증 수행
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.resolveTokenFromRequest(request);

        //토큰 유효성 검증
        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
            Long userId = tokenProvider.getUserId(token); // token 으로부터 userId 읽어오기
            
            //유저와 토큰 일치 시 userDetails 생성
            UserDetails userDetails = this.authService.loadUserByUsername(userId.toString());

            //레디스에서 로그아웃중인 경우
            if (redis.hasKey(tokenProvider.getId(token))) {
                throw new AlreadySignOutException();
            }

            //UserDetails, Password, Role -> 접근권한 인증 Token 생성
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

            //현재 Request Security Context 접근권한 설정
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info(String.format("[%s] -> %s", this.tokenProvider.getUserId(token), request.getRequestURI()));
        }

        filterChain.doFilter(request, response); //다음 필터로 넘기기
    }

    //request 에서 토큰 가져오기
    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
