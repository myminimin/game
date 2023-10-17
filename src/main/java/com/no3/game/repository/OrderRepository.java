package com.no3.game.repository;

import com.no3.game.constant.OrderStatus;
import com.no3.game.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc"
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable); // 현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회

    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
    )
    Long countOrder(@Param("email") String email); // 현재 로그인한 회원의 주문 개수가 몇 개인지 조회

    @Query("SELECT o.orderStatus FROM Order o JOIN o.orderItems oi WHERE o.member.email = :email AND oi.item.id = :itemId")
    Optional<OrderStatus> findOrderStatusByEmailAndItemId(String email, Long itemId);
    // 현재 로그인한 회원의 구매한 아이템 조회용

}
