package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // ワンタイムパスページ
    @GetMapping("/onetimepass")
    public String oneTimePass() {
        return "main/onetimepass";
    }

    @GetMapping("/student/attendance")
    public String showAttendancePage() {
        return "main/attendance_list";
    }

     // 出席詳細ページ
    @GetMapping("/attendance/detail")
    public String showAttendanceDetailPage() {
        return "main/attendance_detail";
    }

    @GetMapping("/cert/list")
    public String showPage() {
        return "main/certificate_list";
    }

}