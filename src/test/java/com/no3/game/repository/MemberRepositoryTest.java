package com.no3.game.repository;

import com.no3.game.constant.Role;
import com.no3.game.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1,5).forEach(i -> {
            Member member = Member.builder()
                    .email("a"+i +"@naver.com")
                    .password("1234")
                    .name("test"+i)
                    .role(Role.USER)
                    .build();
            memberRepository.save(member);
        }); // USER insert test

    }

}