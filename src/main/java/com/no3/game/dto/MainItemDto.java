package com.no3.game.dto;

import com.no3.game.constant.ItemSellStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MainItemDto {

    private Long id;

    private String itemNm; // title

    private String itemDetail; // detail

    private String imgUrl;

    private Integer price;

    private String genre;     // 장르

    private String developer; // 개발사

    private ItemSellStatus itemSellStatus;


    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl,Integer price,
                       String genre, String developer, ItemSellStatus itemSellStatus){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.genre = genre;
        this.developer = developer;
        this.itemSellStatus = itemSellStatus;
    }
}