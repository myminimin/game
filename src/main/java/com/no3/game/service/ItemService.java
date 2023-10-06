package com.no3.game.service;

import com.no3.game.dto.ItemDto;
import com.no3.game.dto.ItemImgDto;
import com.no3.game.dto.PageRequestDTO;
import com.no3.game.dto.PageResultDTO;
import com.no3.game.entity.Item;
import com.no3.game.entity.ItemImg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface ItemService {

    Long register(ItemDto itemDto);

    PageResultDTO<ItemDto, Object[]> getList(PageRequestDTO requestDTO); //목록 처리

    default ItemDto entitiesToDTO(Item item, List<ItemImg> itemImages, Double avg, Long reviewCnt){
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .regDate(item.getRegDate())
                .modDate(item.getModDate())
                .build();

        List<ItemImgDto> itemImgDtoList = itemImages.stream().map(itemImg -> {
            return ItemImgDto.builder().imgName(itemImg.getImgName())
                    .path(itemImg.getPath())
                    .uuid(itemImg.getUuid())
                    .build();
        }).collect(Collectors.toList());

        itemDto.setImgDTOList(itemImgDtoList);
        itemDto.setAvg(avg);
        itemDto.setReviewCnt(reviewCnt.intValue());

        return itemDto;

    }

    default Map<String, Object> dtoToEntity(ItemDto itemDto){

        Map<String, Object> entityMap = new HashMap<>();

        Item item = Item.builder()
                .id(itemDto.getId())
                .price(itemDto.getPrice())
                .title(itemDto.getTitle())
                .genre(itemDto.getGenre())
                .developer(itemDto.getDeveloper())
                .detail(itemDto.getDetail())
                .relaseyear(itemDto.getRelaseyear())
                .build();

        entityMap.put("item", item);

        List<ItemImgDto> imgDTOList = itemDto.getImgDTOList();

        if(imgDTOList != null && imgDTOList.size() > 0 ) {
            List<ItemImg> itemImgList = imgDTOList.stream().map(itemImgDto -> {

                    ItemImg itemImg = ItemImg.builder()
                            .path(itemImgDto.getPath())
                            .imgName(itemImgDto.getImgName())
                            .uuid(itemImgDto.getUuid())
                            .item(item)
                            .build();
                    return itemImg;
            }).collect(Collectors.toList());

            entityMap.put("imgList", itemImgList);
        }

        return entityMap;
    }

}
