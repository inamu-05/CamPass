// File: src/main/java/com/example/demo/StaffRepository.java
package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.entity.Course;

// import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    // Spring automatically provides methods like save(), findAll(), findById(), etc.
    // You can add custom methods here if needed, like:
    // List<Course> findByCourseName(String name);
}