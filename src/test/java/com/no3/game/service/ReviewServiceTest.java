package com.no3.game.service;

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
                .itemTitle("eee")
                .writerEmail("ogi@gmail.com")
                .writerName("명오기")
                .build();

        Long id = reviewService.register(dto);

    }


}