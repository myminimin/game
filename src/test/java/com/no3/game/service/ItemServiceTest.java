package com.no3.game.service;

import com.no3.game.constant.ItemSellStatus;
import com.no3.game.dto.ItemFormDto;
import com.no3.game.dto.ReviewDto;
import com.no3.game.entity.Item;
import com.no3.game.entity.ItemImg;
import com.no3.game.entity.Review;
import com.no3.game.repository.ItemImgRepository;
import com.no3.game.repository.ItemRepository;
import com.no3.game.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception{
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            String path = "C:/shop/item/";
            String imageName = "image" + i +".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});

            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception{
        ItemFormDto itemFormDto = new ItemFormDto();

        itemFormDto.setItemNm("테스트 상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품입니다.");
        itemFormDto.setPrice(1000);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemNm(), item.getTitle());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getReviewsByItemIdTest() throws Exception {
        // 먼저 Item과 Review를 미리 생성합니다.
        Item testItem = Item.builder()
                .title("테스트 상품")
                .price(1000)
                .detail("테스트 상품입니다.")
                // 기타 필요한 testItem 설정 ...
                .build();
        itemRepository.save(testItem);

        Review review1 = Review.builder()
                .item(testItem)
                .text("리뷰1")
                // 기타 필요한 review1 설정 ...
                .build();

        Review review2 = Review.builder()
                .item(testItem)
                .text("리뷰2")
                // 기타 필요한 review2 설정 ...
                .build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        // 이제 위에서 생성한 Item의 itemId를 사용하여 getReviewsByItemId를 테스트합니다.
        List<ReviewDto> reviewDtoList = reviewService.getReviewsByItemId(testItem.getId());

        // 반환된 ReviewDto 목록의 크기와 내용을 확인합니다.
        assertEquals(2, reviewDtoList.size());
        assertTrue(reviewDtoList.stream().anyMatch(r -> r.getText().equals("리뷰1")));
        assertTrue(reviewDtoList.stream().anyMatch(r -> r.getText().equals("리뷰2")));
    }


}