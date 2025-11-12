package com.example.app.controller;

import com.example.app.entity.Subject;
import com.example.app.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    /**
     * Retrieves all subjects assigned to the currently logged-in user (teacher).
     */
    @GetMapping
    public ResponseEntity<List<Subject>> getSubjectsForTeacher(Authentication authentication) {
        // The Authentication object provides details about the currently logged-in user.
        // Assuming the principal name is the user_id (e.g., "admin").
        String teacherId = authentication.getName(); 
        
        List<Subject> subjects = subjectService.findSubjectsByTeacherId(teacherId);
        
        return ResponseEntity.ok(subjects);
    }
}