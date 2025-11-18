package com.example.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.app.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    // List<Student> findByStudentName(String name);
    Student findByUserId(String userId);
    
    // 学生IDまたは学生名で部分一致検索
    List<Student> findByUserIdContainingOrUserNameContaining(String userId, String userName);
}