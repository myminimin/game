package com.no3.game.controller;

import com.no3.game.dto.PageRequestDTO;
import com.no3.game.dto.ReviewDto;
import com.no3.game.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/review")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/register")
    public void register(){

    }

    @PostMapping("/register")
    public String register(ReviewDto reviewDto, RedirectAttributes redirectAttributes){
        log.info("reviewDto: " + reviewDto);

        Long id = reviewService.register(reviewDto);

        redirectAttributes.addFlashAttribute("msg", id);

        return "redirect:/review/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("Review list..." + pageRequestDTO);

        model.addAttribute("result", reviewService.getList(pageRequestDTO));
    }

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

        return "redirect:/review/read";

    }

}
