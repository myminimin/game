package com.no3.game.entity;

import lombok.*;
import com.no3.game.constant.Role;
import com.no3.game.dto.MemberJoinDto;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // (카카오 로그인 적용할 때 변경하기)
    private String name;

    @Column(unique = true) // 이메일을 유일하게 구분(동일값이 db에 들어올 수 없음)
    private String email;
    private String password;

    /* 카카오 회원가입 */
    // private boolean social;

    public void changePassword(String password){
        this.password = password;
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberJoinDto memberJoinDto, PasswordEncoder passwordEncoder){

        Member member = Member.builder()
                .name(memberJoinDto.getName())
                .email(memberJoinDto.getEmail())
                .password( passwordEncoder.encode( memberJoinDto.getPassword() ) ) // BCryptPasswordEncoder Bean 을 파라미터로 넘겨서 비번을 암호화함
                .role(Role.USER)  // 유저
                //.role(Role.ADMIN)   // 관리자
                .build();

        return member;
    }

}