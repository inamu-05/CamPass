// File: src/main/java/com/example/app/service/AttendanceService.java
package com.example.app.service;

import com.example.app.dto.AttendanceHistoryDto;
import com.example.app.dto.AttendanceRequest;
import com.example.app.entity.Attendance;
import com.example.app.entity.AttendanceId;
import com.example.app.entity.Student;
import com.example.app.entity.Subject;
import com.example.app.repository.AttendanceRepository;
import com.example.app.repository.RosterRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.SubjectRepository;
import com.example.app.repository.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
// import java.util.Arrays;
import java.util.List;
// import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StaffRepository staffRepository;

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
    public LocalDateTime recordAttendance(AttendanceRequest request, LocalDateTime sessionDatetime) {
        String subjectId = request.getSubjectId();
        String userId = request.getUserId();

        // final Duration OTP_EXPIRY_DURATION = Duration.ofMinutes(5);
        // LocalDateTime expirationTime = sessionDatetime.plus(OTP_EXPIRY_DURATION).truncatedTo(ChronoUnit.SECONDS);

        // System.out.println("Flutter sessionDatetime: "+sessionDatetime);
        // System.out.println("Flutter expirationTime: "+expirationTime);
        // 1. Find the existing pre-populated attendance record (the "absent" record)
        Attendance attendanceRecord = attendanceRepository
            .findByIdSubjectIdAndIdUserIdAndIdSessionDatetime(subjectId, userId, sessionDatetime);

        if (attendanceRecord == null) {
            // This student either wasn't registered for the class or the teacher didn't pre-populate the record.
            throw new RuntimeException("Attendance record not found for this session.");
        }

        LocalDateTime DateTimeNow = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));

        final Duration OTP_ONTIME_DURATION = Duration.ofMinutes(5);
        LocalDateTime lateTime = sessionDatetime.plus(OTP_ONTIME_DURATION).truncatedTo(ChronoUnit.SECONDS);

        if (DateTimeNow.isAfter(lateTime)) {
            // Late attendance
            attendanceRecord.setRemark("1"); // Example: '2' for Late
        } else {
            // On-time attendance
            attendanceRecord.setRemark("3"); // Example: '1' for On-time
        }

        // 2. Update the fields
        attendanceRecord.setIsAttended(true);
        attendanceRecord.setAttendedOn(DateTimeNow);
        // attendanceRecord.setRemark("0"); // Example: '1' for Present

        // 3. Use the JpaRepository save() method!
        // Because the primary key (subjectId, userId, sessionDatetime) already exists, 
        // save() performs an SQL UPDATE operation.
        attendanceRepository.save(attendanceRecord);
        return DateTimeNow; 
    }


    /**
     * Finds all students for a subject and pre-populates the attendance table 
     * with 'Absent' records for the current session.
     * @param subjectId The ID of the subject.
     * @param sessionDatetime The unique identifier for this class session.
     */
    @Transactional
    public void prePopulateAttendance(String subjectId, LocalDateTime sessionDatetime) {

        // final Duration OTP_EXPIRY_DURATION = Duration.ofMinutes(5);
        // LocalDateTime expirationTime = sessionDatetime.plus(OTP_EXPIRY_DURATION).truncatedTo(ChronoUnit.SECONDS);
        
        // 1. Get the list of student IDs for this subject using the RosterRepository
        List<String> studentIds = rosterRepository.findStudentIdsBySubjectId(subjectId);
        System.out.println(subjectId);
        System.out.println(studentIds);
        
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

    // NEW METHOD
    public List<AttendanceHistoryDto> getAttendanceHistory(String studentId) {
        // Fetch all attendance records for the student, ordered by session date
        System.out.println("Atendance Service");
        List<Attendance> records = attendanceRepository.findByIdUserIdOrderByIdSessionDatetimeDesc(studentId);
        System.out.println(records);
        return records.stream()
            .map(record -> {
                // Get Subject Name (assuming Subject entity is eagerly loaded or accessible via the Attendance entity's relationship)
                String subjectName = record.getSubject().getSubjectName(); 
                
                // Get Teacher Name (The subject entity has user_id which is the teacher's ID)
                String teacherId = record.getSubject().getStaff().getUserId(); 
                String teacherName = staffRepository.findById(teacherId)
                                                .map(user -> user.getUserName())
                                                .orElse("不明な教員");

                return new AttendanceHistoryDto(
                    subjectName,
                    record.getSessionDatetime(),
                    teacherName,
                    record.getRemark()
                );
            })
            .collect(Collectors.toList());
    }
}