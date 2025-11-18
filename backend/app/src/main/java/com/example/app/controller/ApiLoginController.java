package com.example.app.controller;

import com.example.app.controller.LoginRequest;
import com.example.app.entity.Student;
import com.example.app.entity.User;
import com.example.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class ApiLoginController {

    private static final Logger log = LoggerFactory.getLogger(ApiLoginController.class);

    @Autowired
    private AuthService authService; // Using the service you already built!

    /**
     * Handles student login requests from the Flutter app.
     */
    @PostMapping("/student/login")
    public ResponseEntity<?> loginStudent(@RequestBody LoginRequest loginRequest) {

        log.info("Attempting login for userId: {}", loginRequest.getUserId());

        System.out.println("Student/loginついた");

        log.info("Attempting login for userId: {}", loginRequest.getUserId());
        
        // AuthService to check credentials
        User user = authService.login(loginRequest.getUserId(), loginRequest.getPassword());

        System.out.println(user.getClass().getSimpleName());
        System.out.println(authService.login(loginRequest.getUserId(), loginRequest.getPassword()).getClass().getSimpleName());

        // Check if login was successful AND the user is a Student
        if (user != null && user instanceof Student) {
            
            // Cast the user to a Student
            Student student = (Student) user;

            // Instead of returning 'student' directly create a clean Map with only the data we want to send.
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", student.getUserId());
            response.put("userName", student.getUserName());
            response.put("furigana", student.getFurigana());
            response.put("courseId", student.getCourse().getCourseId());
            response.put("email", student.getMail());
            response.put("img", student.getImg());
            response.put("enrollmentStatus", student.getEnrollmentStatus());
            response.put("entryYear", student.getEntryYear());
            response.put("graduationYear", student.getGraduationYear());
            response.put("isDisabled", student.getIsDisabled());
            

            // IMPORTANT: Never send the password back to the client
            student.setUserPass(null); 
            
            // Send back the Student data on success (HTTP 200 OK)
            // The Flutter app can use this data (e.g., to show "Welcome, [Name]")
            return ResponseEntity.ok(student); 
        }

        log.warn("Login FAILED for userId: {}", loginRequest.getUserId()); // Log failure
        // If login fails or user is not a student, send "Unauthorized" (HTTP 401)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("学生番号またはパスワードが間違っています。");
    }
}