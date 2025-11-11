// In backend/app/src/main/java/com/example/app/controller/StudentController.java
package com.example.app.controller;

import com.example.app.entity.Course;
import com.example.app.entity.Student;
import com.example.app.service.CourseService;
import com.example.app.service.FileStorageService;
import com.example.app.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/student") // All URLs in this controller will start with /student
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FileStorageService fileStorageService;

    // 学生一覧表示
    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "main/student-list"; // templates/main/student-list.html
    }

    // 学生登録フォーム表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("student", new Student()); 
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "main/student_register"; // templates/main/student_register.html
    }

    // 学生登録処理
    @PostMapping("/register")
    public String registerStudent(@ModelAttribute("student") Student student, @RequestParam("file") MultipartFile file) throws IOException{
        // デフォルト値の設定
        student.setEnrollmentStatus("1");
        student.setIsDisabled(false); 
        
        // 画像ファイル名の作成（学生番号を利用）
        String studentId = student.getUserId();
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = studentId + fileExtension;

        // 画像ファイルをFileStorageServiceで保存
        String filePath = fileStorageService.storeFile(file, filename);
        student.setImg(filePath);

        // 学生情報をDBに保存
        studentService.createStudent(student);

        // 登録完了ページへリダイレクト
        return "redirect:/student/register/complete";
    }

    // 登録完了画面表示
    @GetMapping("/register/complete")
    public String showRegisterComplete() {
        return "main/ster_comp"; // Corresponds to src/main/resources/templates/main/ster_comp.html
    }

    // 学生情報更新画面
    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("isNew", false);
        return "main/student-form"; // Re-use the same form
    }

    /**
     * UPDATE (Process Form)
     * URL: POST /student/edit/{id}
     * Saves the changes to the existing student.
     */
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable String id, @ModelAttribute("student") Student studentDetails) {
        studentService.updateStudent(id, studentDetails);
        return "redirect:/students"; // Redirect back to the student list
    }

    // /**
    //  * DELETE
    //  * URL: GET /student/delete/{id}
    //  * Deletes the student.
    //  */
    // @GetMapping("/delete/{id}")
    // public String deleteStudent(@PathVariable String id) {
    //     studentService.deleteStudent(id);
    //     return "redirect:/student";
    // }
}