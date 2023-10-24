package com.no3.game.service;

import com.no3.game.entity.Cart;
import com.no3.game.entity.Member;
import com.no3.game.entity.Order;
import com.no3.game.entity.Review;
import com.no3.game.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional // 로직을 처리하다 에러가 발생하면 변경된 데이터를 로직을 수행하기 이전 상태로 콜백 시켜줌
@RequiredArgsConstructor
@Log4j2
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

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
            // - 사용자가 이름과 비밀번호로 로그인을 시도할 때 생성되는 토큰
            // - 인증이 성공하면, 이 토큰은 인증된 사용자의 정보를 포함
            // - 여기에서 getName()은 인증된 유저의 name을 반환
            String email = ((UsernamePasswordAuthenticationToken) principal).getName();
            return memberRepository.findByEmail(email).orElse(null);

        } else if (principal instanceof OAuth2AuthenticationToken) {
            // 소셜 로그인 시 -
            // - 소셜 로그인 서비스를 통해 사용자가 인증될 때 생성되는 토큰
            // - 이 토큰은 인증된 사용자의 세부 정보를 포함
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            // 인증된 사용자의 세부 정보를 담고 있는 'OAuth2User' 객체를 가져옴
            String email = oauth2User.getAttribute("email");
            // 'OAuth2User' 객체에서 이메일 주소를 추출.
            // 로그 추가
            log.info("Using OAuth2AuthenticationToken, email: {}", email);

            Member member = memberRepository.findByEmail(email).orElse(null);

            // 로그 추가
            if (member == null) {
                log.warn("No member found for email: {}", email);
            } else {
                log.info("Member found: {}", member);
            }

            return member;
        } else {
            throw new IllegalArgumentException("Unsupported principal type");
            // 어떤 타입에도 속하지 않을 때 예외 발생(예상치 못한 타입의 'Principal' 객체가 주어졌을 때 문제 발견을 위한 목적)
        }
    }

    public boolean deleteMember(String email, String password) {
        // 회원 탈퇴

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));

        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart != null) {
            cartItemRepository.deleteByCartId(cart.getId());
            cartRepository.delete(cart);
        }

        // Remove the member from orders
        List<Order> orders = orderRepository.findByMemberId(member.getId());

        if (orders != null) {
            for (Order order: orders) {
                order.setMember(null);
                orderRepository.save(order);
            }
        }

        // Delete review by member
        List<Review> reviews = reviewRepository.findByMemberId(member.getId());

        if (reviews != null) {
            reviewRepository.deleteReviewByMember(member);
        }

        // Check the password and delete the member
        if (passwordEncoder.matches(password, member.getPassword())) {
            memberRepository.delete(member);
            return true;
        } else {
            return false;
        }
    }

}
