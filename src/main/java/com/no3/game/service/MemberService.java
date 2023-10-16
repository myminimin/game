package com.no3.game.service;

import com.no3.game.entity.Member;
import com.no3.game.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional // 로직을 처리하다 에러가 발생하면 변경된 데이터를 로직을 수행하기 이전 상태로 콜백 시켜줌
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.isPresent()){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        } // 이미 가입된 회원의 경우 IllegalStateException 예외를 발생
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 로그인할 유저의 email을 파라미터로 전달 받음

        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        Member member = memberOptional.orElseThrow(() -> new UsernameNotFoundException(email));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public Member getMemberFromPrincipal(Principal principal) {

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            // 일반 로그인 시 -
            // - 사용자가 이름과 비밀번호로 로그인을 시도할 때 생성되는 토큰입니다.
            // - 인증이 성공하면, 이 토큰은 인증된 사용자의 정보를 포함하게 됩니다.
            // - 여기에서 getName()은 인증된 주체의 식별자(대부분 이메일 또는 ID)를 반환합니다.
            String email = ((UsernamePasswordAuthenticationToken) principal).getName();
            return memberRepository.findByEmail(email).orElse(null);

        } else if (principal instanceof OAuth2AuthenticationToken) {
            // 소셜 로그인 시 -
            // - 소셜 로그인 서비스를 통해 사용자가 인증될 때 생성되는 토큰입니다.
            // - 이 토큰은 인증된 사용자의 세부 정보를 포함하게 됩니다.
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            // 인증된 사용자의 세부 정보(예: 이름, 이메일, 프로필 사진 등)를 담고 있는 'OAuth2User' 객체를 가져옵니다.
            String email = oauth2User.getAttribute("email");
            // 'OAuth2User' 객체에서 이메일 주소를 추출합니다. 이는 소셜 로그인 제공자가 제공하는 정보 중 하나입니다.
            return memberRepository.findByEmail(email).orElse(null);
            // 해당 이메일로 'Member' 객체를 데이터베이스에서 찾아 반환합니다.

        } else {
            throw new IllegalArgumentException("Unsupported principal type");
            // 어떤 타입에도 속하지 않을 때 예외 발생(예상치 못한 타입의 'Principal' 객체가 주어졌을 때 문제 발견을 위한 목적)
        }
    }

}
