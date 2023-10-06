package com.no3.game.controller;

import com.no3.game.dto.ItemDto;
import com.no3.game.dto.PageRequestDTO;
import com.no3.game.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
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

        return "redirect:item/itemList";
    }

    @GetMapping(value = "/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        model.addAttribute("result", itemService.getList(pageRequestDTO));

    }

}

