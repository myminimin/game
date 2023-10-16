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

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            // 일반 로그인 시 -
            // - 사용자가 이름과 비밀번호로 로그인을 시도할 때 생성되는 토큰입니다.
            // - 인증이 성공하면, 이 토큰은 인증된 사용자의 정보를 포함하게 됩니다.
            // - 여기에서 getName()은 인증된 주체의 식별자(대부분 이메일 또는 ID)를 반환합니다.
            String email = ((UsernamePasswordAuthenticationToken) principal).getName();
            return memberRepository.findByEmail(email).orElse(null);

        } else if (principal instanceof OAuth2AuthenticationToken) {
            // 소셜 로그인 시 -
            // - 소셜 로그인 서비스를 통해 사용자가 인증될 때 생성되는 토큰입니다.
            // - 이 토큰은 인증된 사용자의 세부 정보를 포함하게 됩니다.
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            // 인증된 사용자의 세부 정보(예: 이름, 이메일, 프로필 사진 등)를 담고 있는 'OAuth2User' 객체를 가져옵니다.
            String email = oauth2User.getAttribute("email");
            // 'OAuth2User' 객체에서 이메일 주소를 추출합니다. 이는 소셜 로그인 제공자가 제공하는 정보 중 하나입니다.
            return memberRepository.findByEmail(email).orElse(null);
            // 해당 이메일로 'Member' 객체를 데이터베이스에서 찾아 반환합니다.


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
