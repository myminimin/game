package com.no3.game.dto;

import com.no3.game.constant.ItemSellStatus;
import com.no3.game.entity.Item;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemFormDto {

    private Long id;

    @NotNull(message = "상품명은 필수 입력 값입니다.")
    private String itemNm; // title

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotNull(message = "내용은 필수 입력 값입니다.")
    private String itemDetail; // detail

    private String genre;

    private String developer;

    private LocalDate relaseyear;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 상품 저장 후 수정할 때 상품 이비지 정보를 저장하는 리스트

    private List<Long> itemImgIds = new ArrayList<>(); // 상품의 이미지 아이디를 저장하는 리스트

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return Item.builder()
                .title(this.getItemNm())
                .detail(this.getItemDetail()) // 여기에서 detail 필드에 값을 설정해야 합니다.
                .price(this.getPrice())
                .itemSellStatus(this.itemSellStatus)
               .build();
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }

}
