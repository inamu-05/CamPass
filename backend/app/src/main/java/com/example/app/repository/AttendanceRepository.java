package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.entity.Attendance;
import com.example.app.entity.AttendanceId;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Repository interface for the Attendance entity, which uses a composite primary key.
 * * The generic parameters are:
 * 1. Attendance: The Entity class.
 * 2. AttendanceId: The class representing the composite primary key (@Embeddable).
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {
    
    // --- Custom Query Examples ---
    
    /**
     * Finds all attendance records for a specific student (userId).
     * Spring Data JPA can traverse the composite key fields automatically.
     * * @param userId The ID of the student
     * @return A list of Attendance records
     */
    List<Attendance> findByIdUserId(String userId);

    /**
     * Finds all attendance records for a specific subject (subjectId).
     * * @param subjectId The ID of the subject
     * @return A list of Attendance records
     */
    List<Attendance> findByIdSubjectId(String subjectId);

    /**
     * Finds all attendance records for a specific student and subject.
     * This demonstrates querying by both fields in the composite key.
     */
    List<Attendance> findByIdUserIdAndIdSubjectId(String userId, String subjectId);

    /**
     * Finds all attendance records for a specific student, subject and session datatime.
     * This demonstrates querying by both fields in the composite key.
     */
    Attendance findByIdSubjectIdAndIdUserIdAndIdSessionDatetime(String subjectId, String userId, LocalDateTime sessionDatetime);

    /**
     * Finds all attendance records for a specific session datatime.
     * This demonstrates querying by both fields in the composite key.
     */
    Attendance findByIdSessionDatetime(LocalDateTime sessionDatetime);

    /**
     * Finds all records where the student attended (isAttended = TRUE)
     */
    List<Attendance> findByIsAttendedTrue();

    /**
     * Finds all records attended after a specific date/time.
     */
    List<Attendance> findByAttendedOnAfter(LocalDateTime dateTime);

    List<Attendance> findByIdUserIdOrderByIdSessionDatetimeDesc(String studentId);
}
