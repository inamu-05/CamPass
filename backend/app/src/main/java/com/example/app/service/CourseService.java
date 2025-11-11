package com.example.app.service;

import com.example.app.entity.Course;
import com.example.app.repository.CourseRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;

    // コース取得メソッド　
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
