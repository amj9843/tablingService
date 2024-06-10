package com.zerobase.tabling.data.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.tabling.annotation.NullOrNot;
import com.zerobase.tabling.annotation.PhoneNumber;
import com.zerobase.tabling.annotation.ValidEnum;
import com.zerobase.tabling.data.constant.UserRole;
import com.zerobase.tabling.data.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {
    //회원가입 요청 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpRequest {
        @NotBlank(message = "아이디 입력은 필수이며 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String id;

        @NotBlank(message = "비밀번호 입력은 필수이며 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String password;

        @NotBlank(message = "이름 입력은 20자 이내의 문자가 필수이며" +
                " 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        @Size(max = 20, message = "이름 입력은 20자 이내여야 합니다.")
        private String username;

        @PhoneNumber
        @NotNull(message = "핸드폰 번호 입력은 필수이며 없는 경우 빈 칸을 허용합니다.\n" +
                "단, 형식을 맞춰주세요. 형식 -> '010-1234-1234' 혹은 '01012341234'")
        private String phoneNumber;

        @ValidEnum(enumClass = UserRole.class,
        message = "USER|PARTNER 중 하나를 입력해야 합니다.")
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
        @NotBlank(message = "아이디 입력은 필수이며 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String id;

        @NotBlank(message = "비밀번호 입력은 필수이며 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String password;
    }

    //비밀번호 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserPassword {
        @NotBlank
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
        @NotBlank(message = "기존 비밀번호 입력은 필수이며 빈 칸, 공백으로만 구성되는 것은 허용하지 않습니다.")
        private String originPassword;

        @NullOrNot
        private String password;

        @NullOrNot
        @Size(max = 20)
        private String username;

        @PhoneNumber
        private String phoneNumber;
    }

    //매장 예약 내역 조회, 리뷰 조회 등 다른 조회에서 유저 정보를 보여야할 때 사용될 응답 DTO
    @Data
    @NoArgsConstructor
    @Builder
    public static class ForResponse {
        private Long id;
        private String name;

        @QueryProjection
        public ForResponse(Long userId, String username) {
            this.id = userId;
            this.name = username;
        }
    }
}
