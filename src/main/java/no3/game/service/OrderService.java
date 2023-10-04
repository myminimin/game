package no3.game.service;

import lombok.RequiredArgsConstructor;
import no3.game.entity.Member;
import no3.game.entity.Order;
import no3.game.repository.MemberRepository;
import no3.game.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email); // 현재 로그인한 유저의 이메일 가져오기
        Order order = orderRepository.findById(orderId) // 주문 정보 가져오기
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember(); // 주문 정보에 있는 유저의 정보를 가져오기

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        } // 현재 로그인한 유저의 이메일과 주문 정보에 있는 이메일이 같지 않으면 false

        return true; // 같으면 true

    } // DB에 있는 email과 주문자 email을 대조

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    } // 주문 취소

}
