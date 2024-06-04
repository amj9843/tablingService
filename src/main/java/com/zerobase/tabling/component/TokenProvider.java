package com.zerobase.tabling.component;

import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.constant.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${spring.jwt.expire-time}")
    private long accessTokenExpireTime;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    /**
     * JWT 생성
     *
     * @param user 로그인한 유저의 정보 객체
     * @return JWT String
     */
    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId());

        claims.put("userId", user.getUserId());
        claims.put("role", user.getRole());

        var now = new Date();
        var expiredDate = new Date(now.getTime() + this.accessTokenExpireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(this.secretKey.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * TOKEN 으로부터 로그인한 user 의 아이디 추출
     * @param token 발급받은 토큰
     * @return id
     */
    public String getId(String token) {
        return this.parseClaims(token).getSubject();
    }

    /**
     * TOKEN 으로부터 로그인한 user 식별번호 추출
     * @param token 발급받은 토큰
     * @return userId
     */
    public Long getUserId(String token) {
        return this.parseClaims(token).get("userId", Long.class);
    }

    /**
     * TOKEN 으로부터 로그인한 user 역할 추출
     * @param token 발급받은 토큰
     * @return role
     */
    public UserRole getRole(String token) {
        return this.parseClaims(token).get("role", UserRole.class);
    }

    /**
     * 토큰 유효성 검사
     * @param token 발급받은 토큰
     * @return true/false
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            log.error("토큰이 비었습니다.");

            return false;
        }

        Claims claims = this.parseClaims(token);
        if (claims.getExpiration().before(new Date())) {
            log.error("토큰의 유효기간이 만료되었습니다.");

            return false;
        }

        return true;
    }

    /**
     * JWT Claims 추출
     * @param token 발급받은 토큰
     * @return JWT Claims
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(this.secretKey.getBytes())).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getAccessTokenExpireTime() {
        return this.accessTokenExpireTime;
    }
}
