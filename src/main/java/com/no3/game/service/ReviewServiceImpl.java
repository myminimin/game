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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long register(ReviewDto dto) {
        log.info(dto);

        Item item = itemRepository.findByTitle(dto.getItemNm())
                .orElseThrow(() -> new IllegalArgumentException("No item found with title: " + dto.getItemNm()));

        Member member = memberRepository.findByEmail(dto.getWriterEmail())
                .orElseThrow(() -> new IllegalArgumentException("No member found with email: " + dto.getWriterEmail()));

        Review review = Review.builder()
                .id(dto.getId())
                .text(dto.getText())
                .grade(dto.getGrade())
                .item(item)
                .member(member)
                .build();

        reviewRepository.save(review);
        return review.getId();
    } // 리뷰 작성

    @Override
    public PageResultDTO<ReviewDto, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], ReviewDto> fn = (en -> entityToDTO((Review)en[0],(Member)en[1],(Item)en[2]));

       /* Page<Object[]> result = repository.getReviewWithAll(
                pageRequestDTO.getPageable(Sort.by("id").descending())  );*/
        Page<Object[]> result = reviewRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("id").descending())  );


        return new PageResultDTO<>(result, fn);
    }

    @Override
    public ReviewDto get(Long id) {

        Object result = reviewRepository.getReviewById(id);

        Object[] arr = (Object[])result;

        return entityToDTO((Review)arr[0], (Member)arr[1], (Item) arr[2]);

    } // read 구현

    @Transactional
    @Override
    public void modify(ReviewDto reviewDto) {

        Optional<Review> result = reviewRepository.findById(reviewDto.getId());

        Review review = result.get();
        review.changeGrade(reviewDto.getGrade());
        review.changeText(reviewDto.getText());
        reviewRepository.save(review);

    } // modify 구현

    @Override
    public List<ReviewDto> getReviewsByItemId(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid itemId"));
        List<Review> reviews = reviewRepository.findByItem(item);

        return reviews.stream()
                .map(review -> entityToDTO(review, review.getMember(), review.getItem()))
                .collect(Collectors.toList());
    } // itemId에 해당하는 리뷰들을 가지고 옴
}
