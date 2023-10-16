package com.no3.game.service;

import com.no3.game.dto.CartDetailDto;
import com.no3.game.dto.CartItemDto;
import com.no3.game.dto.CartOrderDto;
import com.no3.game.dto.OrderDto;
import com.no3.game.entity.Cart;
import com.no3.game.entity.CartItem;
import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import com.no3.game.repository.CartItemRepository;
import com.no3.game.repository.CartRepository;
import com.no3.game.repository.ItemRepository;
import com.no3.game.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;


    public Long addCart(CartItemDto cartItemDto, Member member){

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + cartItemDto.getItemId()));

        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null){
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item);
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    public Member getMemberFromPrincipal(Principal principal) {
        if (principal instanceof UserDetails) {
            // 특정 객체가 해당 클래스의 인스턴스인지를 확인
            String username = ((UserDetails) principal).getUsername();
            return memberRepository.findByEmail(username).orElse(null);

        }else if (principal instanceof OAuth2AuthenticationToken) {
            // 소셜 로그인 시 = OAuth2 인증을 위한 토큰을 나타내는 클래스
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            // 실제 사용자 정보를 포함하는 'OAuth2User' 객체를 가져옴
            String email = oauth2User.getAttribute("email");
            // 이메일 주소를 가져옴
            return memberRepository.findByEmail(email).orElse(null);
            // 해당 이메일로 'Member' 객체를 데이터베이스에서 찾아 반환

        } else if (principal instanceof UsernamePasswordAuthenticationToken) {
            // 일반 로그인 시 = 스프링 시큐리티에서 사용자 이름과 비밀번호를 기반으로 한 인증한 경우
            String email = ((UsernamePasswordAuthenticationToken) principal).getName();
            // 여기에서 name은 실제 이름이 아니라 사용자를 식별하는 값
            return memberRepository.findByEmail(email).orElse(null);
            // 그 값을 이용해 'Member' 객체를 데이터베이스에서 찾아 반환
        } else {
            throw new IllegalArgumentException("Unsupported principal type");
            // 어떤 타입에도 속하지 않을 때 예외 발생(예상치 못한 타입의 'Principal' 객체가 주어졌을 때 문제 발견을 위한 목적)
        }
    }



    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with Email: " + email));


        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){

        Member curMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with Email: " + email));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }

}
