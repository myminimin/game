package com.no3.game.dto;

import com.no3.game.entity.OrderItem;
import lombok.Data;

@Data
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        // orderItem 객체와 이미지 경로를 파라미터로 받아서 멤버 변수 값을 세팅

        this.itemNm = orderItem.getItem().getTitle();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm; //상품명
    private int count; //주문 수량

    private int orderPrice; //주문 금액
    private String imgUrl; //상품 이미지 경로

}