package com.no3.game.repository;

import com.no3.game.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Member findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update Member m set m.password =:password where m.id = :id ")
    void updatePassword(@Param("password") String password, @Param("id") String id); // 비밀번호 수정


}
