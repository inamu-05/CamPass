package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.app.entity.Staff;
import com.example.app.entity.Student;
import com.example.app.repository.StaffRepository;
import com.example.app.repository.StudentRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // ユーザー名に基づいてユーザー情報を取得し、UserDetailsオブジェクトを返すロジックを実装
        // 学生テーブルから検索
        Student student = studentRepository.findByUserId(userId);
        if (student != null) {
            return User.withUsername(student.getUserId())
                .password(student.getUserPass())
                .roles("STUDENT")
                .build();
        }

        // 職員テーブルから検索
        Staff staff = staffRepository.findByUserId(userId);
        if (staff != null) {
            return User.withUsername(staff.getUserId())
                .password(staff.getUserPass())
                .roles("STAFF")
                .build();
        } 

        throw new UsernameNotFoundException("ユーザーが見つかりません。: " + userId);
    }
}