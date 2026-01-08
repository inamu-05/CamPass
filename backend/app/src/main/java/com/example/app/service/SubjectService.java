// File: src/main/java/com/example/app/service/SubjectService.java
package com.example.app.service;

// import com.example.app.controller.AttendanceRequest;
import com.example.app.entity.Subject;
import com.example.app.entity.Student;
import com.example.app.entity.User;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.SubjectRepository;
import com.example.app.repository.StaffRepository;
import com.example.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    SubjectService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all subjects (you can filter this by teacher later)
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    public String findSubjectNameById(String subjectId) {
        // Use the repository to find the subject entity
        return subjectRepository.findById(subjectId)
        
            // Use map to safely get the subjectName if present
            .map(subject -> subject.getSubjectName())
            // Provide a default fallback name if the ID is invalid
            .orElse("Unknown Subject"); 
    }

    /**
     * Finds all subjects assigned to a specific user (teacher).
     * Assuming Subject entity has a field 'userId' that links to the teacher.
     */
    public List<Subject> findSubjectsByTeacherId(String teacherId) {
        // You might need a custom method in your SubjectRepository:
        // List<Subject> findByUserId(String userId);
        return subjectRepository.findByStaff(staffRepository.findByUserId(teacherId)); 
    }

    // A crucial method you need:
    public Optional<Subject> getSubjectById(String subjectId) {
        return subjectRepository.findById(subjectId);
    }

    public List<Subject> getSubjectsByStudentId(String studentId) {
        // 1. Find the User record for the student to get the Course ID
        User user = userRepository.findByUserId(studentId);

        if (user == null) {
            throw new RuntimeException("Student user not found with ID: " + studentId);
        }

        // User user = userOptional.get();
        // Assuming your User entity has a reference to the Course entity
        // private Course course;
        if (user.getCourse() == null) {
             throw new RuntimeException("Student's course information is missing.");
        }
        
        // 2. Use the new repository method to fetch subjects by the Course object
        // This leverages the relationship Subject(course) = User(course)
        return subjectRepository.findByCourse(user.getCourse());
        
        // // --- Dummy Data for Quick Testing (Replace with real logic) ---
        // Subject s1 = new Subject(); 
        // s1.setSubjectId("JOHO1");
        // s1.setSubjectName("情報処理Ⅰ");
        // // ... set other fields
        
        // Subject s2 = new Subject(); 
        // s2.setSubjectId("KEIEI2");
        // s2.setSubjectName("経営戦略Ⅱ");
        // // ... set other fields

        // return List.of(s1, s2); // Return a list of subjects
    }
}