package com.no3.game.repository;

import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // Member findByEmail(String email);
    // 중복된 회원이 있는지 검사하기 위해서 이메일로 회원을 검사할 수 있게 쿼리 메소드를 작성

    Optional<Member> findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("update Member m set m.password =:password where m.id = :id ")
    void updatePassword(@Param("password") String password, @Param("id") Long id); // 비밀번호 수정


}
