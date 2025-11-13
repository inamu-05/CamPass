// In backend/app/src/main/java/com/example/app/service/StudentService.java
package com.example.app.service;

import com.example.app.entity.Student;
import com.example.app.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 学生一覧を取得　
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 学生をIDで取得
    public Student getStudentById(String userId) {
        return studentRepository.findById(userId).orElse(null);
    }

    // 学生情報登録
    public Student createStudent(Student student) {
        // パスワードをハッシュ化して保存
        student.setUserPass(passwordEncoder.encode(student.getUserPass()));
        return studentRepository.save(student);
    }

    // 学生情報更新
    public Student updateStudent(String userId, Student studentDetails) {
        // 1. Find the existing student
        Student existingStudent = getStudentById(userId);
        if (existingStudent == null) {
            return null; // Or throw an exception
        }

        // 更新情報の取得と設定
        existingStudent.setUserId(userId);
        existingStudent.setUserName(studentDetails.getUserName());
        existingStudent.setClassGroup(studentDetails.getClassGroup());
        // existingStudent.setCourseId(studentDetails.getCourseId());
        // existingStudent.setBirth(studentDetails.getBirth());
        existingStudent.setTel(studentDetails.getTel());
        existingStudent.setMail(studentDetails.getMail());
        existingStudent.setAddress(studentDetails.getAddress());
        // existingStudent.setImg(studentDetails.getImg());
        existingStudent.setEnrollmentStatus(studentDetails.getEnrollmentStatus());
        // existingStudent.setEntryYear(studentDetails.getEntryYear());
        // existingStudent.setGraduationYear(studentDetails.getGraduationYear());
        existingStudent.setIsDisabled(studentDetails.getIsDisabled());

        // // 3. Check if a new password was provided
        // String newPassword = studentDetails.getUserPass();
        // if (newPassword != null && !newPassword.isEmpty()) {
        //     // If yes, hash and set the new password
        //     existingStudent.setUserPass(passwordEncoder.encode(newPassword));
        // }
        // // If no new password was provided, we do nothing (keeping the old one)

        // 学生情報を更新して保存
        return studentRepository.save(existingStudent);
    }

    // DELETE
    public void deleteStudent(String userId) {
        studentRepository.deleteById(userId);
    }
}