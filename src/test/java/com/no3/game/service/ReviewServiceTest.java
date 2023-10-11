package com.no3.game.service;

import com.no3.game.dto.PageRequestDTO;
import com.no3.game.dto.PageResultDTO;
import com.no3.game.dto.ReviewDto;
import com.no3.game.entity.Item;
import com.no3.game.repository.ItemRepository;
import com.no3.game.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Test
    public void testRegister() {

        ReviewDto dto = ReviewDto.builder()
                .text("재밌어요")
                .grade((int)(Math.random()*5) + 1)
                .itemNm("테스트 상품")
                .writerEmail("ogi@naver.com")
                .writerName("오기")
                .build();

        Long id = reviewService.register(dto);

    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<ReviewDto, Object[]> result = reviewService.getList(pageRequestDTO);

        for (ReviewDto reviewDto : result.getDtoList()) {
            System.out.println(reviewDto);
        }
    }


}