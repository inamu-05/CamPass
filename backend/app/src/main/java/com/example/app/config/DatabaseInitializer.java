package com.example.app.config;

import com.example.app.entity.ClassGroup;
import com.example.app.entity.Course;
import com.example.app.entity.Staff;
import com.example.app.entity.Student;
import com.example.app.repository.ClassGroupRepository;
import com.example.app.repository.StaffRepository;
import com.example.app.repository.StudentRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Ensures a default test user is present in the database on application startup.
 * This is the preferred method for guaranteed initialization when the password 
 * needs to be hashed by the application's configured PasswordEncoder.
 */
@Configuration
public class DatabaseInitializer {

    // Define a Bean that runs code on application startup
    @Bean
    public CommandLineRunner initDatabase(
            StaffRepository staffRepository, 
            StudentRepository studentRepository,
            ClassGroupRepository classGroupRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {

            final ClassGroup classId = new ClassGroup("01","1-1");

            final String initialStaffId = "admin";
            final String initialPassword = "adminpass"; // Plain text password for test user
            final Course initialCourseId = new Course("01", "情報"); 

            // 1. Check if the user already exists to prevent duplicate creation
            // We use findById() which is safe to call before the user is saved.
            if (staffRepository.findById(initialStaffId).isEmpty()) {
                
                // 2. Hash the password using the application's configured encoder
                String hashedPassword = passwordEncoder.encode(initialPassword);

                // 3. Create the user entity
                Staff initialStaff = new Staff(
                    initialStaffId, 
                    "Admin Test User", 
                    "アドミン",
                    initialCourseId, 
                    hashedPassword,
                    "0"
                );

                // 4. Save the user to the database
                staffRepository.save(initialStaff);
                System.out.println("-> Staff 'admin' created.");
                
                // Console output for confirmation
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Initial user created successfully:");
                System.out.println("ID: " + initialStaffId + " | Pass: " + initialPassword);
                System.out.println("-----------------------------------------------------------------");
            }

            final String stu1Id = "2201001";
            final String initialStudentPassword = "stupass";
            if (studentRepository.findById(stu1Id).isEmpty()) {
                String hashedPassword = passwordEncoder.encode(initialStudentPassword);
                
                Student student1 = new Student(
                    stu1Id, 
                    "学生太郎", 
                    "ガクセイタロウ",
                    initialCourseId, 
                    hashedPassword,
                    java.time.LocalDate.of(2000, 1, 1), // birth
                    "090-1234-5678", // tel
                    "2201001@test.com", // mail
                    "東京都", // address
                    classId,
                    "/path/to/img1.jpg", // img
                    "0", // enrollment_status
                    "2022", // entry_year
                    null, // graduation_year
                    false // is_disabled
                );
                studentRepository.save(student1);
                System.out.println("-> Student '2201001' created.");
            }

            // Student 2
            final String stu2Id = "2201002";
            if (studentRepository.findById(stu2Id).isEmpty()) {
                String hashedPassword = passwordEncoder.encode(initialStudentPassword);
                
                Student student2 = new Student(
                    stu2Id, 
                    "学生花子", 
                    "ガクセイハナコ",
                    initialCourseId, 
                    hashedPassword,
                    java.time.LocalDate.of(2001, 5, 15), // birth
                    "090-8765-4321", // tel
                    "2201002@test.com", // mail
                    "大阪府", // address
                    classId,
                    "/path/to/img2.jpg", // img
                    "0", // enrollment_status
                    "2022", // entry_year
                    null, // graduation_year
                    false // is_disabled
                );
                studentRepository.save(student2);
                System.out.println("-> Student '2201002' created.");
            }
            
            System.out.println("-----------------------------------------------------------------");
            System.out.println("Database initialization complete.");
            System.out.println("-----------------------------------------------------------------");
        
        };
    }
}
