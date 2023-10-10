package com.no3.game.service;

import com.no3.game.dto.ItemDto;
import com.no3.game.dto.PageRequestDTO;
import com.no3.game.dto.PageResultDTO;
import com.no3.game.entity.Item;
import com.no3.game.entity.ItemImg;
import com.no3.game.repository.ItemImgRepository;
import com.no3.game.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository imgRepository;

    @Transactional
    @Override
    public Long register(ItemDto itemDto) {

        Map<String, Object> entityMap = dtoToEntity(itemDto);
        Item item = (Item) entityMap.get("item");
        List<ItemImg> itemImgList = (List<ItemImg>) entityMap.get("imgList");

        itemRepository.save(item);

        if (itemImgList != null) { // itemImgList가 null이 아닌 경우에만 foreach 실행
            itemImgList.forEach(itemImg -> {
                imgRepository.save(itemImg);
            });
        }

        return item.getId();
    }

    @Override
    public PageResultDTO<ItemDto, Object[]> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("id").descending());

        Page<Object[]> result = itemRepository.getListPage(pageable);

        log.info("==============================================");
        result.getContent().forEach(arr -> {
            log.info(Arrays.toString(arr));
        });


        Function<Object[], ItemDto> fn = (arr -> entitiesToDTO(
                (Item) arr[0] ,
                (List<ItemImg>)(Arrays.asList((ItemImg)arr[1])),
                (Double) arr[2],
                (Long)arr[3])
        );

        return new PageResultDTO<>(result, fn);
    }

}
