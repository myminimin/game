package com.no3.game.controller;

import com.no3.game.dto.CartDetailDto;
import com.no3.game.dto.CartItemDto;
import com.no3.game.dto.CartOrderDto;
import com.no3.game.entity.Member;
import com.no3.game.repository.MemberRepository;
import com.no3.game.service.CartService;
import com.no3.game.service.ItemService;
import com.no3.game.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            Member member = memberService.getMemberFromPrincipal(principal);
            Long cartItemId = cartService.addCart(cartItemDto, member);
            return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
        } catch(EntityNotFoundException ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    } // 장바구니 버튼 클릭 시

    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){
        Member member = memberService.getMemberFromPrincipal(principal); // 현재 사용자의 Member 엔티티를 가져와
        List<CartDetailDto> cartDetailList = cartService.getCartList(member.getEmail()); // 이 멤버의 Email을 전달
        model.addAttribute("cartItems", cartDetailList);
        return "cart/cartList";
    } // 장바구니 페이지


    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){

        if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    } // 수정 기능인 것 같은데 현재 사용 안함

    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){

        if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    } // 장바구니 아이템 삭제

    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){

        Member member = memberService.getMemberFromPrincipal(principal);
        if (member == null) {
            return new ResponseEntity<String>("로그인 정보가 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }

        for (CartOrderDto cartOrder : cartOrderDtoList) {
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), member.getEmail())){
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList, member.getEmail());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    } // 장바구니 상품 구매


}
