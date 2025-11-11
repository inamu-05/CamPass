package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 学生登録ページ
    @GetMapping("/student/register")
    public String showStudentRegisterPage() {
        return "main/student_register";
    }

    // 登録完了ページ
    @GetMapping("/student/register/comp")
    public String registerStudent() {
        return "main/register_comp";
    }

    // 学生検索ページ
    @GetMapping("/student/search")
    public String studentSearch() {
        return "main/student_search";
    }

    // 学生更新ページ
    @GetMapping("/student/update")
    public String showStudentUpdatePage() {
        return "main/student_update";  // templates/main/student_update.html
    }

    // 更新完了ページ
    @GetMapping("/student/update/comp")
    public String showUpdateCompletePage() {
        return "main/update_completed";  // templates/main/update_completed.html に対応
    }

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
