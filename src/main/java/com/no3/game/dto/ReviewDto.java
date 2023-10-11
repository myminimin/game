package com.no3.game.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id; // 리뷰 번호
    private String text;    // 리뷰 내용
    private int grade;      // 평점

    private String itemNm; // 아이템 이름
    private String writerEmail; // 작성자의 이메일
    private String writerName; // 작성자의 이름

    private LocalDateTime regTime;    // 등록일
    private LocalDateTime updateTime; // 수정일

}
