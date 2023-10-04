package no3.game.entity;

import lombok.*;
import no3.game.constant.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(unique = true) // 이메일을 유일하게 구분(동일값이 db에 들어올 수 없음)
    private String email;
    private String password;

    /* 카카오 회원가입 */
    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roleSet = new HashSet<>();

    public void changePassword(String password){
        this.password = password;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void addRole(Role Role){
        this.roleSet.add(Role);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(boolean social){this.social = social;}

    /*@Enumerated(EnumType.STRING) // enum 타입은 순서로 저장되는데 String으로 저장하기 권장함
    private Role role; // Enum에 등록되어 있는 role을 사용함*/

   /* public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();  // Member 클래스를 새로 생성함
        member.setName(memberFormDto.getName());  // memberFormDto로 전달받은 값을 getName()으로 저장함
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());  // 패스워드는 암호화로 처리함
        member.setPassword(password);
        member.setRole(Role.ADMIN);  // 룰은 admin으로 등록함.
        return member; // member 객체를 리턴함.
    }*/

}