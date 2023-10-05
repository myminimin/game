package com.no3.game.repository;

import com.no3.game.entity.Item;
import com.no3.game.entity.ItemImg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemImgRepository itemImgRepository;

    @Commit
    @Transactional
    @Test
    public void insertItems() {

        IntStream.rangeClosed(1,5).forEach(i -> {

            Item item = Item.builder()
                    .title("item...." +i)
                    .price(i)
                    .genre("action")
                    .developer("nexon")
                    .detail("detail..." +i)
                    .build();

            System.out.println("------------------------------------------");

            itemRepository.save(item);

            int count = (int)(Math.random() * 5) + 1; //1,2,3,4


            for(int j = 0; j < count; j++){
                ItemImg itemImage = ItemImg.builder()
                        .uuid(UUID.randomUUID().toString())
                        .item(item)
                        .imgName("test"+j+".jpg").build();

                itemImgRepository.save(itemImage);
            }


            System.out.println("===========================================");

        });

    } // insert item

    @Test
    public void testListPage(){

        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Object[]> result = itemRepository.getListPage(pageRequest);

        for (Object[] objects : result.getContent()) {
            System.out.println(Arrays.toString(objects));
        }
    } // paging

    @Test
    public void testGetItemWithAll() {

        List<Object[]> result = itemRepository.getItemWithAll(8L);

        System.out.println(result);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }

    }
}