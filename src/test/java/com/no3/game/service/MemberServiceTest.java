package com.no3.game.service;

import com.no3.game.dto.MemberJoinDto;
import com.no3.game.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional  // 테스트 클래스에 선언 시 테스트 실행 수 롤백 처리됨 (메소드를 반복 테스트 가능)
@TestPropertySource(locations="classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setEmail("ogi@naver.com");
        memberJoinDto.setName("오기");
        memberJoinDto.setPassword("1234");
        return Member.createMember(memberJoinDto, passwordEncoder);
    } // 회원 정보를 입력한 Member 엔티티를 만드는 메소드

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
        // assertEquals 메소드를 이용해 저장하려고 요청했던 값과 실제 저장된 데이터를 비교함
        // assertEquals(기대 값, 실제로 저장된 값)
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);});
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}
