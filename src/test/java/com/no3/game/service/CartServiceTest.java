package com.no3.game.service;

import com.no3.game.constant.ItemSellStatus;
import com.no3.game.dto.CartItemDto;
import com.no3.game.entity.CartItem;
import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import com.no3.game.repository.CartItemRepository;
import com.no3.game.repository.ItemRepository;
import com.no3.game.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){
        Item item = Item.builder()
                .title("빌더 패턴 수정 후 테스트 상품")
                .price(10000)
                .detail("테스트 상품 상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .build();
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = Member.builder()
                        .email("builder@test.com")
                        .build();
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(item.getId());

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
    }


}
