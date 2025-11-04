package com.example.app.config;

import com.example.app.entity.Staff;
import com.example.app.repository.StaffRepository;
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
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            final String initialStaffId = "admin";
            final String initialPassword = "adminpass"; // Plain text password for test user
            final String initialCourseId = "01"; 
            final String initialStaffCategory = "0";

            // 1. Check if the user already exists to prevent duplicate creation
            // We use findById() which is safe to call before the user is saved.
            if (staffRepository.findById(initialStaffId).isEmpty()) {
                
                // 2. Hash the password using the application's configured encoder
                String hashedPassword = passwordEncoder.encode(initialPassword);

                // 3. Create the user entity
                Staff initialStaff = new Staff(
                    initialStaffId, 
                    "Admin Test User", 
                    initialCourseId, 
                    hashedPassword,
                    initialStaffCategory
                );

                // 4. Save the user to the database
                staffRepository.save(initialStaff);
                
                // Console output for confirmation
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Initial user created successfully:");
                System.out.println("ID: " + initialStaffId + " | Pass: " + initialPassword);
                System.out.println("-----------------------------------------------------------------");
            }
        };
    }
}
