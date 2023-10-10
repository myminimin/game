package com.no3.game.controller;

import com.no3.game.dto.ItemDto;
import com.no3.game.dto.PageRequestDTO;
import com.no3.game.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/admin/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/new")
    public String itemForm(){
        return "item/itemForm";
    }

    @PostMapping(value = "/new")
    public String itemForm(ItemDto itemDto, RedirectAttributes redirectAttributes){
        Long item_id = itemService.register(itemDto);

        redirectAttributes.addFlashAttribute("msg", item_id);

        return "redirect:itemList";
    }

    @GetMapping(value = "/list")
    public void itemList(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO : " + pageRequestDTO);

        model.addAttribute("result", itemService.getList(pageRequestDTO));

    }

}

