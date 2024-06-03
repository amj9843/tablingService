package com.zerobase.tabling.service.impl;

import com.zerobase.tabling.component.RedisComponent;
import com.zerobase.tabling.component.TokenProvider;
import com.zerobase.tabling.data.domain.User;
import com.zerobase.tabling.data.dto.AuthDto;
import com.zerobase.tabling.data.repository.UserRepository;
import com.zerobase.tabling.exception.impl.AlreadyExistIdException;
import com.zerobase.tabling.exception.impl.IncorrectPasswordException;
import com.zerobase.tabling.exception.impl.NoUserException;
import com.zerobase.tabling.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final UserRepository userRepository;
    //비밀번호 암호화
    private final PasswordEncoder passwordEncoder;
    //토큰 관련(생성, 정보 가져오기 등)
    private final TokenProvider tokenProvider;
    //레디스 관련
    private final RedisComponent redis;

    @Override
    @Transactional
    public AuthDto.UserIdentifiedInfo signup(AuthDto.SignUpRequest userDto) {
        //아이디 중복확인
        boolean exists = this.userRepository.existsById(userDto.getId());
        if (exists) {
            throw new AlreadyExistIdException();
        }
        
        //비밀번호는 암호화하여 세팅
        userDto.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        //DB에 저장
        User user = this.userRepository.save(userDto.toEntity());

        //보여줄 값만 빼서 return
        return new AuthDto.UserIdentifiedInfo(user.getUserId(), user.getId());
    }

    @Override
    @Transactional
    public AuthDto.UserToken signin(AuthDto.SignInRequest signInDto) {
        //로그인하려는 아이디가 회원가입 되어있는지 확인
        User user = this.userRepository.findById(signInDto.getId())
                .orElseThrow(NoUserException::new);
        
        //비밂번호가 일치하는지 확인
        if (!this.passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        //토큰 생성
        AuthDto.UserToken token = new AuthDto.UserToken(this.tokenProvider.generateToken(user));
        //토큰 생성까지 완료하면 로그인한 유저 정보 로그로 기록
        log.info(String.format("user login -> [userId] %d, [id] %s", user.getUserId(), user.getId()));
        //레디스 블랙리스트에 아이디가 등록되어 있다면 삭제
        if (redis.hasKey(user.getId())) redis.deleteValues(user.getId());

        return token;
    }

    @Override
    public void signout(String id) {
        //레디스에 토큰 사용 못하도록 등록
        redis.setValues(id, "signOutUser", Duration.ofMillis(this.tokenProvider.getAccessTokenExpireTime()));
    }

    @Override
    @Transactional
    public void modifiedInfo(Long userId, AuthDto.ModifiedInfoRequest modifiedInfoRequest) {
        User user = this.userRepository.findByUserId(userId).orElseThrow(NoUserException::new);
        
        //등록된 비밀번호와 입력한 원래 비밀번호가 일치하는지 확인
        if (!this.passwordEncoder.matches(modifiedInfoRequest.getOriginPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        
        //modifiedInfoRequest 값이 있는 항목은 새 값으로, 없으면 기존값 유지
        String password = (modifiedInfoRequest.getPassword() != null) ?
                this.passwordEncoder.encode(modifiedInfoRequest.getPassword()) : user.getPassword();
        String username = (modifiedInfoRequest.getUsername() != null) ?
                modifiedInfoRequest.getUsername() : user.getUsername();
        String phoneNumber = (modifiedInfoRequest.getPhoneNumber() != null) ?
                modifiedInfoRequest.getPhoneNumber() : user.getPhoneNumber();
        
        //업데이트
        user.update(password, username, phoneNumber);
    }

    @Override
    @Transactional
    public void delete(Long userId, AuthDto.UserPassword password) {
        User user = this.userRepository.findByUserId(userId).orElseThrow(NoUserException::new);

        //등록된 비밀번호와 입력한 비밀번호가 일치하는지 확인
        if (!this.passwordEncoder.matches(password.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        this.userRepository.deleteByUserId(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return this.userRepository.findByUserId(Long.parseLong(userId))
                .orElseThrow(NoUserException::new);
    }
}
