package com.example.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.entity.Staff;
import com.example.app.entity.Student;
import com.example.app.entity.Subject;
import com.example.app.entity.Course;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {

    List<Subject> findByStaff(Staff staff);
    // List<Subject> findBySubjectName(String name);
    // List<Subject> findSubjectsEnrolledByStudent(Optional<Student> student);

    /**
     * Finds all subjects that belong to the specified Course.
     * This infers the subjects the student (who is in that Course) is enrolled in.
     * Assumes the Subject entity has a 'course' field defined:
     * @ManyToOne @JoinColumn(name = "course_id") private Course course;
     */
    List<Subject> findByCourse(Course course);

    // Alternative if you prefer passing the ID directly:
    // List<Subject> findByCourse_CourseId(String courseId);
}