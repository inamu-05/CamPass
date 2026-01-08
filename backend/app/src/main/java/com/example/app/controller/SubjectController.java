package com.example.app.controller;

import com.example.app.entity.Subject;
import com.example.app.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.security.JwtUtil; // Import your JWT utility
import com.example.app.controller.SubjectDto; // Create a simple DTO to hold subject_id and subject_name
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SubjectService subjectService;

    /**
     * Fetches subjects taught by the user/teacher extracted from the JWT.
     * The student ID is NOT used here, as the subjects are tied to the teacher.
     * This call should be made by a student who is viewing the list of classes they can attend.
     * Since your 'subject' table ties to 'user_id' (teacher), this is a common approach.
     * If the 'subject' table was directly tied to the student's enrollment, the logic would change.
     */
    @GetMapping("/by-teacher")
    public ResponseEntity<?> getSubjectsByTeacher(
        @RequestHeader(name = "Authorization") String tokenHeader) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
        }

        try {
            String token = tokenHeader.substring(7); // Remove "Bearer "
            String teacherId = jwtUtil.extractUserId(token); // Assuming extractUsername works for all user types // changed Username for UserId

            // Fetch subjects (e.g., List<SubjectDto>)
            List<Subject> subjects = subjectService.findSubjectsByTeacherId(teacherId);

            if (subjects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No subjects found for this teacher.");
            }
            
            // Return the list of simplified subject objects
            return ResponseEntity.ok(subjects); 
        } catch (Exception e) {
            // Handle token validation failure, expired token, etc.
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token.");
        }
    }

    // ================================== Anterior =======================================
    // /**
    //  * Retrieves all subjects assigned to the currently logged-in user (teacher).
    //  */
    // @GetMapping
    // public ResponseEntity<List<Subject>> getSubjectsForTeacher(Authentication authentication) {
    //     // The Authentication object provides details about the currently logged-in user.
    //     // Assuming the principal name is the user_id (e.g., "admin").
    //     String teacherId = authentication.getName(); 
        
    //     List<Subject> subjects = subjectService.findSubjectsByTeacherId(teacherId);
        
    //     return ResponseEntity.ok(subjects);
    // }

    @GetMapping("/current-student")
    @PreAuthorize("hasRole('STUDENT')") // Check for the ROLE_STUDENT authority
    public ResponseEntity<List<Subject>> getSubjectsForCurrentStudent(Authentication authentication) {
        // 1. Get the student ID from the authenticated user (assuming the principal holds the user ID)
        String userId = authentication.getName(); // Assuming JWT principal is the user ID/Student ID
        
        // 2. Fetch the subjects
        List<Subject> subjects = subjectService.getSubjectsByStudentId(userId);
        
        if (subjects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(subjects);
    }
}