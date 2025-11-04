package com.example.app.service;

import com.example.app.entity.Staff;
import com.example.app.entity.Student;
import com.example.app.entity.User;
import com.example.app.repository.StaffRepository;
import com.example.app.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StaffRepository staffRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ユーザー認証メソッド
    public User login(String userId, String password) {
        // 学生テーブルから検索
        Student student = studentRepository.findByUserId(userId);
        if (student != null && passwordEncoder.matches(password, student.getUserPass())) {
            return student;
        }

        // 職員テーブルから検索
        Staff staff = staffRepository.findByUserId(userId);
        if (staff != null && passwordEncoder.matches(password, staff.getUserPass())) {
            return staff;
        }

        // 認証失敗
        return null;
    }
}
