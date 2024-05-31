package com.zerobase.tabling.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    //user 식별번호(pk)
    private Long userId;

    @Column(name = "id")
    //아이디
    private String id;

    @Column(name = "password")
    //비밀번호
    private String password;

    @Column(name = "user_name")
    //사용자 이름
    private String name;

    @Column(name = "phone_number")
    //사용자 핸드폰번호
    private String phoneNumber;

    @Column(name = "roles")
    //사용자 권한
    private List<String> roles;

    @OneToMany(mappedBy = "user")
    //다대일 양방향 매핑 연관관계 지정: '일'에 해당
    private List<Store> stores = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    //다대일 양방향 매핑 연관관계 지정: '일'에 해당
    private List<Reservation> reservations = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.name;
    }
    
    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    
    //계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }
    
    //비밀번호가 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    
    //계정 활성화 상태 여부
    @Override
    public boolean isEnabled() {
        return false;
    }
    
    //User 생성자
    public User(String id, String password, String name, String phoneNumber, List<String> roles) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }
}
