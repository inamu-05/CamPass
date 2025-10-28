package com.example.app.entity;
// File: src/main/java/com/example/demo/Staff.java

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity; // Note: Spring Boot 2 uses javax.persistence
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;

@Entity // Marks this class as a JPA entity (maps to a database table)
public class Course {

    @Id // Primary key
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "course_name")
    private String courseName;

    // Default constructor (required by JPA)
    public Course() {}

    // Constructor for easy object creation
    public Course(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    // --- Getters and Setters ---
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
}
