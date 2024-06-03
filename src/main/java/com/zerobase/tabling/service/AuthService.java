package com.zerobase.tabling.service;

import com.zerobase.tabling.data.dto.AuthDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    //회원가입 서비스
    AuthDto.UserIdentifiedInfo signup(AuthDto.SignUpRequest signUpDto);

    //로그인 서비스
    AuthDto.UserToken signin(AuthDto.SignInRequest signInDto);

    //로그아웃 서비스
    void signout(String id);

    //회원정보 수정
    void modifiedInfo(Long userId, AuthDto.ModifiedInfoRequest modifiedInfoRequest);

    //회원정보 삭제
    void delete(Long userId, AuthDto.UserPassword password);
}
