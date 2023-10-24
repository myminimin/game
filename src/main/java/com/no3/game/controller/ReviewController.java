package com.no3.game.controller;

import com.no3.game.dto.PageRequestDTO;
import com.no3.game.dto.ReviewDto;
import com.no3.game.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/review")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/register")
    public void register(){

    } // 리뷰 등록 페이지 (사용 안함)

    @PostMapping("/register")
    public String register(ReviewDto reviewDto, RedirectAttributes redirectAttributes){
        log.info("reviewDto: " + reviewDto);

        Long id = reviewService.register(reviewDto);

        redirectAttributes.addFlashAttribute("msg", id);

        return "redirect:review/list";
    } // 리뷰 등록 (사용 안함)

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("Review list..." + pageRequestDTO);

        model.addAttribute("result", reviewService.getList(pageRequestDTO));

    } // 리뷰 전체 출력

    @GetMapping({"/read", "/modify" })
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long id, Model model){

        log.info("review id: " + id);

        ReviewDto reviewDto = reviewService.get(id);

        log.info(reviewDto);

        model.addAttribute("dto", reviewDto);
    }

    @PostMapping("/modify")
    public String modify(ReviewDto dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes){


        log.info("post modify.........................................");
        log.info("dto: " + dto);

        reviewService.modify(dto);

        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());

        redirectAttributes.addAttribute("id",dto.getId());

        return "redirect:review/read";

    }

    @PostMapping("/registerAjax")
    public ResponseEntity<?> registerAjax(@RequestBody ReviewDto reviewDto) {
        try {
            log.info("reviewDto: " + reviewDto);

            Long id = reviewService.register(reviewDto);

            return ResponseEntity.ok("Review saved successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    } // 리뷰 게시판이 아니라 itemDtl에서 AJAX 활용해 댓글 작성하게 처리

}
