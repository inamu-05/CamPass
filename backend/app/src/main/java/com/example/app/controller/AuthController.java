package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    // 職員用ログインページ
    @GetMapping("/login")
    public String login() {
        return "login"; // resources/templates/login.html を返す
    }

    // // 学生用ログインページ(仮)
    // @GetMapping("/api/login")
    // @ResponseBody
    // public String apiLogin() {
    //     return "学生用ログインページ（仮）";
    // }
}
