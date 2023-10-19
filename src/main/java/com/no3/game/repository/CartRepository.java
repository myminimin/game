package com.no3.game.repository;

import com.no3.game.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
