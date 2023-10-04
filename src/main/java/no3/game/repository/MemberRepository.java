package no3.game.repository;

import no3.game.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.id = :id and m.social = false")
    Optional<Member> getWithRoles(String id);

    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update Member m set m.password =:password where m.id = :id ")
    void updatePassword(@Param("password") String password, @Param("id") String id);


}
