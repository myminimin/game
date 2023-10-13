package com.no3.game.auth;

import com.no3.game.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PrincipalDetails implements UserDetails , OAuth2User {

    private Member member; // 컴포지션
    private Map<String, Object> attributes;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    //OAuth 로그인
    public PrincipalDetails(Member member,Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }


    @Override // 추가됨
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 해당 User의 권한을 리턴하는 곳!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> String.valueOf(member.getRole()));
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        // 사이트에서 1년동안 회원이 로그인을 안했을 경우 휴면 계정으로 하기로 함.
        // 현재 시간 - 로그인 시간 => 1년을 초과하면 return false;

        return true;
    }

    @Override // 추가됨 -- 사용은 X
    public String getName() {
        return null;
    }
}
