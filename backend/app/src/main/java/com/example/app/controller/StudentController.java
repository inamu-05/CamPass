// In backend/app/src/main/java/com/example/app/controller/StudentController.java
package com.example.app.controller;

import com.example.app.entity.Course;
import com.example.app.entity.Student;
import com.example.app.entity.ClassGroup;
import com.example.app.service.CourseService;
import com.example.app.service.FileStorageService;
import com.example.app.service.StudentService;
import com.example.app.service.ClassGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;

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
    private ClassGroupService classGroupService;

    @Autowired
    private FileStorageService fileStorageService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

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
        Student student = new Student();
        model.addAttribute("student", student); 

        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);

        List<ClassGroup> classGroups = classGroupService.getAllClassGroups();
        model.addAttribute("classGroups", classGroups);
        return "main/student_register"; // templates/main/student_register.html
    }

    // 学生登録処理
    @PostMapping("/register")
    public String registerStudent(
        @Validated
        @ModelAttribute("student") Student student, 
        BindingResult bindingResult,
        @RequestParam("file") MultipartFile file,
        Model model
    ) {
        // 選択肢と写真ファイルの未選択チェック
        if (student.getCourse() == null || student.getCourse().getCourseId() == null) {
            bindingResult.rejectValue("course", "error.course", "所属学科は必須項目です。");
        }
        if (student.getClassGroup() == null || student.getClassGroup().getClassGroupId() == null) {
            bindingResult.rejectValue("classGroup", "error.classGroup", "クラスは必須項目です。");
        }
        if (file.isEmpty()) {
            model.addAttribute("fileError", "証明写真を選択してください。");
        }

        //エラーチェック
        if (bindingResult.hasErrors() || model.containsAttribute("fileError")) {
            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses); 
            List<ClassGroup> classGroups = classGroupService.getAllClassGroups();
            model.addAttribute("classGroups", classGroups);
            return "main/student_register";
        }

        // デフォルト値の設定
        student.setEnrollmentStatus("1");
        student.setIsDisabled(false); 
        
        try {
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

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "ファイルのアップロードに失敗しました。もう一度お試しください。");
            model.addAttribute("student", student);

            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses); 
            List<ClassGroup> classGroups = classGroupService.getAllClassGroups();
            model.addAttribute("classGroups", classGroups);

            return "main/student_register";
        }
    }

    // 登録完了画面表示
    @GetMapping("/register/complete")
    public String showRegisterComplete() {
        return "main/ster_comp"; // Corresponds to src/main/resources/templates/main/ster_comp.html
    }

    // 学生検索ページ
    @GetMapping("/search")
    public String studentSearch(Model model, @RequestParam(required = false) String keyword) {
        List<Student> students;
        if (keyword == null || keyword.isEmpty()) {
            students = studentService.getAllStudents();
            keyword ="";            
        } else {
            students = studentService.searchStudents(keyword);
        }

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        return "main/student_search";
    }

    // 学生情報更新画面
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable String id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "main/student_update";
    }

    //学生情報更新処理
    @PostMapping("/update/{id}")
    public String updateStudent(
        @PathVariable String id,
        @ModelAttribute("student") Student studentDetails,
        Model model) {

    // 住所が空白の場合
    if (studentDetails.getAddress() == null || studentDetails.getAddress().isBlank()) {

        // 更新前の学生情報を再取得
        Student student = studentService.getStudentById(id);

        model.addAttribute("student", student);
        model.addAttribute("errorMessage", "住所を入力してください");

        // 更新前の画面に戻す
        return "main/student_update";
    }

    // 正常時は更新
    studentService.updateStudent(id, studentDetails);
    return "redirect:/student/update/complete";
    }


    // 更新完了画面表示
    @GetMapping("/update/complete")
    public String showUpdateComplete() {
        return "main/update_completed"; // Corresponds to src/main/resources/templates/main/update_completed.html
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