package com.no3.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// @RequiredArgsConstructor
public class MainController {

    @GetMapping(value="/")
    public String main() {
        return "main";
    } // 회원 가입 후 메인페이지로 이동할 수 있도록 추가

}
