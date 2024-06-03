package com.zerobase.tabling.data.dto;

import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.domain.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {
    //회원가입 요청 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpRequest {
        private String id;
        private String password;
        private String username;
        private String phoneNumber;
        private UserRole role;

        public User toEntity(){
            return User.builder()
                    .id(this.id)
                    .password(this.password)
                    .username(this.username)
                    .phoneNumber(this.phoneNumber)
                    .role(this.role)
                    .build();
        }
    }

    //회원이 갖는 고유 정보 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserIdentifiedInfo {
        private Long userId;
        private String id;
    }
    
    //로그인 요청 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignInRequest {
        private String id;
        private String password;
    }

    //비밀번호 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserPassword {
        private String password;
    }

    //토큰값 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserToken {
        private String token;
    }

    /**
     * 유저 정보 수정 요청 DTO
     * 수정 가능 정보 : 비밀번호, 이름, 핸드폰번호
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifiedInfoRequest {
        //개인정보를 바꾸는 것은 민감한 일이라 기존 비밀번호 한 번 더 확인
        private String originPassword;
        private String password;
        private String username;
        private String phoneNumber;
    }
}
