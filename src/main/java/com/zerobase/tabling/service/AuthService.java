package com.zerobase.tabling.service;

import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.dto.AuthDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    //회원가입 서비스
    AuthDto.UserIdentifiedInfo signup(AuthDto.SignUpRequest signUpDto);

    //로그인 서비스
    AuthDto.UserToken signin(AuthDto.SignInRequest signInDto);

    //로그아웃 서비스
    void signout(User user);

    //회원정보 수정
    void modifiedInfo(User user, AuthDto.ModifiedInfoRequest modifiedInfoRequest);

    //회원정보 삭제
    void delete(User user, AuthDto.UserPassword password);
}
