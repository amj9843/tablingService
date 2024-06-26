package com.zerobase.tabling.controller;

import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.dto.AuthDto;
import com.zerobase.tabling.data.dto.ResultDto;
import com.zerobase.tabling.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 회원가입(로그인이 안된 상태여야만 동작)
     * @param signUpRequest 핸드폰번호 외엔 빈 값 불가
     * Body json
     * {
     *   "id": "tester1", 아이디
     *   "password": "password", 비밀번호
     *   "username": "user", 이름(최대 20자)
     *   "phoneNumber": "010-1234-5678", 핸드폰번호(012-3456-7890 혹은 01012345678 형식 혹은 빈 값(""))
     *   "role": "USER" USER 혹은 PARTNER
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "회원가입이 완료되었습니다.",
     *     "data": {
     *         "userId": 1, 유저 식별번호
     *         "id": "tester1" 가입된 아이디
     *     }
     * }
     */
    @Operation(summary = "회원가입")
    @PreAuthorize("isAnonymous()") //헤더에 토큰이 없어야 실행 가능
    @PostMapping("/sign/up")
    public ResponseEntity<?> signUp(@Validated @RequestBody AuthDto.SignUpRequest signUpRequest) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "회원가입이 완료되었습니다.",
                        this.authService.signup(signUpRequest)));
    }

    /**
     * 로그인(로그인이 안된 상태여야만 동작)
     * @param signInRequest
     * Body json
     * {
     *   "id": "tester1", 아이디
     *   "password": "password" 비밀번호
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "로그인이 완료되었습니다.",
     *     "data": {
     *         "token": "string" 발급받은 토큰
     *     }
     * }
     */
    @Operation(summary = "로그인")
    @PreAuthorize("isAnonymous()") //헤더에 토큰이 없어야 실행 가능
    @PostMapping("/sign/in")
    public ResponseEntity<?> signIn(@Validated @RequestBody AuthDto.SignInRequest signInRequest) {
        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "로그인이 완료되었습니다.",
                        this.authService.signin(signInRequest)));
    }

    /**
     * 로그아웃
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "로그아웃이 완료되었습니다.",
     *     "data": null
     * }
     */
    @Operation(summary = "로그아웃")
    @GetMapping("/sign/out")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal User user) {
        this.authService.signout(user);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "로그아웃이 완료되었습니다."));
    }

    /**
     * 회원정보 수정
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param modifiedInfoRequest (변경할 항목 외엔 제거)
     * Body json
     * {
     *   "originPassword": "password", 기존 비밀번호(필수)
     *   "password": "changedPassword", 바꾸려는 비밀번호(항목이 있다면 빈 값 불가)
     *   "username": "username", 이름(최대 20자, 항목이 있다면 빈 값  불가)
     *   "phoneNumber": "010-1234-1234" 핸드폰번호(012-3456-7890 혹은 01012345678 형식 혹은 빈값(""))
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "회원정보 수정이 완료되었습니다.",
     *     "data": null
     * }
     */
    @Operation(summary = "회원정보 수정")
    @PatchMapping("/modify")
    public ResponseEntity<?> modifiedUserInfo(@AuthenticationPrincipal User user,
                                              @Validated @RequestBody AuthDto.ModifiedInfoRequest modifiedInfoRequest) {
        this.authService.modifiedInfo(user, modifiedInfoRequest);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "회원정보 수정이 완료되었습니다."));
    }

    /**
     * 회원 탈퇴
     * 파트너의 경우 : 관리중인 매장에 진행중인 예약이 있는 경우 탈퇴 불가
     * 일반 사용자의 경우 : 진행중인 예약이 있는 경우 탈퇴 불가
     * @param user
     * Headers
     * {
     *     Authorization: Bearer token
     * }
     * @param password
     * Body json
     * {
     *   "password": "changedPassword" (필수)
     * }
     * @return
     * {
     *     "code": "OK",
     *     "message": "회원 탈퇴가 완료되었습니다.",
     *     "data": null
     * }
     */
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/expire")
    public ResponseEntity<?> deletedUserInfo(@AuthenticationPrincipal User user,
                                             @Validated @RequestBody AuthDto.UserPassword password) {
        this.authService.delete(user, password);

        return ResponseEntity.ok(
                ResultDto.res(HttpStatus.OK,
                        "회원 탈퇴가 완료되었습니다."));
    }
}
