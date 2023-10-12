package com.no3.game.entity;

import com.no3.game.constant.ItemSellStatus;
import com.no3.game.repository.ItemRepository;
import com.no3.game.repository.MemberRepository;
import com.no3.game.repository.OrderItemRepository;
import com.no3.game.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = Item.builder()
                .title("테스트 상품")
                .price(10000)
                .detail("상세설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .build();
        return item;
    }

    public Order createOrder() {
        Order order = new Order();

        for(int i = 0; i < 3; i++) {
            Item item = createItem();

            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();

            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    // Order 엔티티를 저장할 때 연관된 OrderItem 엔티티들도 함께 저장되는지 확인하기 위한 테스트
    // 'Order' 엔티티에 Cascade 옵션이 설정되어 있기 때문에 'OrderItem' 엔티티들이 저장이 가능한 것
    public void cascadeTest() {
        Order order = new Order();

        for(int i = 0; i < 3; i++) {
            Item item = this.createItem();

            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();

            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem); 
            // 각 item에 대한 'OrderItem'을 생성하고 'order'와 연결함
        }

        orderRepository.saveAndFlush(order);
        // 주문('order')을 저장
        em.clear();
        // 영속성 컨텍스트를 클리어하여 1차 캐시를 초기화

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        // 방금 저장한 주문을 데이터베이스에서 다시 조회
        assertEquals(3, savedOrder.getOrderItems().size());
        // 조회한 주문에 연결된 'OrderItem'의 수가 3개인지 확인
    }
    

    @Test
    @DisplayName("고아객체 제거 테스트")
        public void orphanRemovalTest() {
            Order order = this.createOrder();
            order.getOrderItems().remove(0);

            em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrder().getClass());
        orderItem.getOrder().getOrderDate();
        System.out.println("==============================");
    }
}