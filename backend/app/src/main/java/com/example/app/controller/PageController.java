package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // メインページ（base.html）
    @GetMapping("/main")
    public String home() {
        return "base/base"; // templates/base/base.html
    }

    // 学生登録ページ
    @GetMapping("/student/register")
    public String studentRegister() {
        return "main/student_register"; // templates/main/student_register.html
    }

    // 学生検索ページ
    @GetMapping("/student/search")
    public String studentSearch() {
        return "main/student_search";
    }

    // ワンタイムパスページ
    @GetMapping("/onetimepass")
    public String oneTimePass() {
        return "main/onetimepass";
    }
}
