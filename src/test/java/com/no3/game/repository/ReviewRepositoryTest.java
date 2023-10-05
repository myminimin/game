package com.no3.game.repository;

import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import com.no3.game.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertItemReviews() {

        // 10개의 리뷰를 등록
        IntStream.rangeClosed(1,10).forEach(i -> {

            // 게임 번호
            Long item_id = (long)(Math.random()*5) + 8;

            // 회원 번호
            Long member_id = ((long)(Math.random()*5) + 23);
            Member member = Member.builder().id(member_id).build();

            Review itemReview = Review.builder()
                    .member(member)
                    .item(Item.builder().id(item_id).build())
                    .grade((int)(Math.random()*5) + 1)
                    .text("이 게임 쩔어요"+i)
                    .build();

            reviewRepository.save(itemReview);
        });
    }

    @Test
    public void testGetItemReviews() {

        Item item = Item.builder().id(8L).build();

        List<Review> result = reviewRepository.findByItem(item);

        result.forEach(itemReview -> {

            System.out.print(itemReview.getId());
            System.out.print("\t"+itemReview.getGrade());
            System.out.print("\t"+itemReview.getText());
            System.out.print("\t"+itemReview.getMember().getEmail());
            System.out.println("---------------------------");
        });
    } // item id에 해당하는 review 가지고 오기

}