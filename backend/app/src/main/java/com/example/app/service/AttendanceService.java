// File: src/main/java/com/example/app/service/AttendanceService.java
package com.example.app.service;

import com.example.app.controller.AttendanceRequest;
import com.example.app.entity.Attendance;
import com.example.app.entity.AttendanceId;
import com.example.app.entity.Student;
import com.example.app.entity.Subject;
import com.example.app.repository.AttendanceRepository;
import com.example.app.repository.RosterRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.SubjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
// import java.util.Arrays;
import java.util.List;
// import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RosterRepository rosterRepository;

    /**
     * Marks a student as present for a specific session after successful OTP validation.
     * @param subjectId The ID of the subject.
     * @param userId The ID of the student.
     * @param sessionDatetime The date/time of the class session (must be the same as pre-populated record).
     */
    public void recordAttendance(AttendanceRequest request, LocalDateTime sessionDatetime) {
        String subjectId = request.getSubjectId();
        String userId = request.getUserId();
        // 1. Find the existing pre-populated attendance record (the "absent" record)
        Attendance attendanceRecord = attendanceRepository
            .findByIdSubjectIdAndIdUserIdAndIdSessionDatetime(subjectId, userId, sessionDatetime);

        if (attendanceRecord == null) {
            // This student either wasn't registered for the class or the teacher didn't pre-populate the record.
            throw new RuntimeException("Attendance record not found for this session.");
        }

        // 2. Update the fields
        attendanceRecord.setIsAttended(true);
        attendanceRecord.setAttendedOn(LocalDateTime.now());
        attendanceRecord.setRemark("0"); // Example: '1' for Present

        // 3. Use the JpaRepository save() method!
        // Because the primary key (subjectId, userId, sessionDatetime) already exists, 
        // save() performs an SQL UPDATE operation.
        attendanceRepository.save(attendanceRecord); 
    }


    /**
     * Finds all students for a subject and pre-populates the attendance table 
     * with 'Absent' records for the current session.
     * @param subjectId The ID of the subject.
     * @param sessionDatetime The unique identifier for this class session.
     */
    @Transactional
    public void prePopulateAttendance(String subjectId, LocalDateTime sessionDatetime) {
        
        // 1. Get the list of student IDs for this subject using the RosterRepository
        List<String> studentIds = rosterRepository.findStudentIdsBySubjectId(subjectId);
        
        if (studentIds.isEmpty()) {
            throw new RuntimeException("No students found for subject: " + subjectId + ". Cannot pre-populate attendance.");
        }

        // 2. Fetch the Subject entity once (for mapping efficiency)
        Subject subjectEntity = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new RuntimeException("Subject not found: " + subjectId));
        
        // 3. Create and save an 'Absent' record for each student
        for (String userId : studentIds) {
            
            // Fetch the Student entity (required for @MapsId and @ManyToOne)
            Student studentEntity = studentRepository.findById(userId) 
                .orElseThrow(() -> new RuntimeException("Student user not found for ID: " + userId));
            
            // Initialize the Attendance ID (Composite Key)
            AttendanceId id = new AttendanceId(subjectId, userId, sessionDatetime);
            
            Attendance record = new Attendance();
            record.setId(id);
            
            // Set the full relational entities
            record.setSubject(subjectEntity);
            record.setStudent(studentEntity);
            
            // Set the initial status (Absent)
            record.setIsAttended(false);
            record.setAttendedOn(null); 
            record.setRemark("0"); // '0' for Absent
            
            attendanceRepository.save(record); // Insert the new record
        }
    }
}