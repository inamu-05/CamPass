// File: src/main/java/com/example/app/service/SubjectService.java
package com.example.app.service;

// import com.example.app.controller.AttendanceRequest;
import com.example.app.entity.Subject;
import com.example.app.repository.SubjectRepository;
import com.example.app.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StaffRepository staffRepository;

    // Get all subjects (you can filter this by teacher later)
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
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
}