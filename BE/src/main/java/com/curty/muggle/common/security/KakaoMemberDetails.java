package com.curty.muggle.common.security;

import com.curty.muggle.member.entity.Member;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 카카오 멤버 정보를 담아 스프링 시큐리티 UserDetails를 구현한 클래스
 * 인증과 권한 부여에 사용됨
 * 멤버 객체 getter로 사용 가능
 */
@Getter
@AllArgsConstructor
public class KakaoMemberDetails implements UserDetails {
    private final Member member;

    /**
     * 비밀번호는 카카오 로그인이라 따로 없어서 빈 문자열 반환
     */
    @Override
    public String getPassword() {
        return "";
    }

    /**
     * 인증 시 사용자 아이디로 사용할 이메일 반환
     */
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    /**
     * 사용자의 권한 정보를 스프링 시큐리티가 인식할 수 있도록 변환하여 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(member.getRole().name()));
    }
}
