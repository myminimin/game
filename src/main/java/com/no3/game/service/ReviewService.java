package com.no3.game.service;

import com.no3.game.dto.PageRequestDTO;
import com.no3.game.dto.PageResultDTO;
import com.no3.game.dto.ReviewDto;
import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import com.no3.game.entity.Review;

public interface ReviewService {

    Long register(ReviewDto dto); // 리뷰 작성

    PageResultDTO<ReviewDto, Object[]> getList(PageRequestDTO pageRequestDTO);

    default Review dtoToEntity(ReviewDto dto) {

        Item item = Item.builder().title(dto.getItemTitle()).build();
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Review review = Review.builder()
                .id(dto.getId())
                .text(dto.getText())
                .grade(dto.getGrade())
                .item(item)
                .member(member)
                .build();
        return review;
    }

    default ReviewDto entityToDTO(Review review, Member member, Item item) {

        ReviewDto reviewDto = ReviewDto.builder()
                .id(review.getId())
                .itemTitle(item.getTitle())
                .text(review.getText())
                .regDate(review.getRegDate())
                .modDate(review.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .build();

        return reviewDto;

    }

}
