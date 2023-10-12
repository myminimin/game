package com.no3.game.service;

import com.no3.game.constant.ItemSellStatus;
import com.no3.game.constant.OrderStatus;
import com.no3.game.dto.OrderDto;
import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import com.no3.game.entity.Order;
import com.no3.game.entity.OrderItem;
import com.no3.game.repository.ItemRepository;
import com.no3.game.repository.MemberRepository;
import com.no3.game.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveItem(){ // 주문할 상품을 저장
        Item item = Item.builder()
                .title("빌더 패턴 수정 후 테스트 상품")
                .price(10000)
                .detail("테스트 상품 상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .build();
        return itemRepository.save(item);
    }

    public Member saveMember(){ // 회원 정보를 저장
        Member member = Member.builder()
                .email("builder@test.com")
                .build();
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();

        int totalPrice = orderDto.getCount()*item.getPrice();

        assertEquals(totalPrice, order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
    }

}
