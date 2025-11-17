package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.example.app.entity.Student;
import com.example.app.service.StudentService;

import java.util.List;


@Controller
public class PageController {
    @Autowired
    private StudentService studentService;

    // 学生検索ページ
    @GetMapping("/student/search")
    public String studentSearch(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "main/student_search";
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