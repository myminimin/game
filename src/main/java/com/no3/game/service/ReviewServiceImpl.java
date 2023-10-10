package com.no3.game.service;

import com.no3.game.dto.PageRequestDTO;
import com.no3.game.dto.PageResultDTO;
import com.no3.game.dto.ReviewDto;
import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import com.no3.game.entity.Review;
import com.no3.game.repository.ItemRepository;
import com.no3.game.repository.MemberRepository;
import com.no3.game.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository repository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long register(ReviewDto dto) {
        log.info(dto);

        Item item = itemRepository.findByTitle(dto.getItemTitle())
                .orElseThrow(() -> new IllegalArgumentException("No item found with title: " + dto.getItemTitle()));

        Member member = memberRepository.findByEmail(dto.getWriterEmail())
                .orElseThrow(() -> new IllegalArgumentException("No member found with email: " + dto.getWriterEmail()));

        Review review = Review.builder()
                .id(dto.getId())
                .text(dto.getText())
                .grade(dto.getGrade())
                .item(item)
                .member(member)
                .build();

        repository.save(review);
        return review.getId();
    }

    @Override
    public PageResultDTO<ReviewDto, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], ReviewDto> fn = (en -> entityToDTO((Review)en[0],(Member)en[1],(Item)en[2]));

       /* Page<Object[]> result = repository.getReviewWithAll(
                pageRequestDTO.getPageable(Sort.by("id").descending())  );*/
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("id").descending())  );


        return new PageResultDTO<>(result, fn);
    }


}
