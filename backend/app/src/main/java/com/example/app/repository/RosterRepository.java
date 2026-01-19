package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.app.entity.Subject; // Using Subject for JpaRepository type, but only for setup
import java.util.List;

@Repository
public interface RosterRepository extends JpaRepository<Subject, String> {

    /**
     * Retrieves a list of user_ids (student IDs) registered for a specific subject
     * by querying the custom subject_roster junction table.
     * * @param subjectId The ID of the subject (e.g., "01").
     * @return A list of student user_id strings.
     */
    @Query(value = "SELECT u.user_id FROM subject s JOIN user u ON s.course_id = u.course_id JOIN student st ON u.user_id = st.user_id WHERE s.subject_id = :subjectId", nativeQuery = true)
    List<String> findStudentIdsBySubjectId(@Param("subjectId") String subjectId);
}