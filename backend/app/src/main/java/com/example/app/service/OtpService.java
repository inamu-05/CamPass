// File: src/main/java/com/example/app/service/OtpService.java
package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.example.app.service.AttendanceService;

@Service
public class OtpService {

    // The template we configured in RedisConfig.java
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Define a 5-minute duration
    // private static final Duration OTP_EXPIRY = Duration.ofMinutes(5);

    // This is a good practice to avoid key collisions
    private static final String KEY_PREFIX = "otp:subject:"; 
    private static final Duration OTP_EXPIRY_DURATION = Duration.ofMinutes(5);

    /**
     * Generates a random 4-digit OTP, saves it to Redis with expiry,
     * pre-populates the attendance table, and returns the full Redis value.
     * * @param subjectId The ID of the subject.
     * @param teacherId The ID of the staff creating the OTP.
     * @return A string formatted as "OTPCode,YYYY-MM-DDTHH:MM:SS"
     */
    @Transactional
    public String saveOtp(String subjectId, String otp, LocalDateTime sessionDatetime) {
        // Calculate expiration time and truncate to seconds for DB consistency
        // LocalDateTime expirationTime = sessionDatetime.plus(OTP_EXPIRY_DURATION).truncatedTo(ChronoUnit.SECONDS);

        // System.out.println("expirationTime: "+expirationTime);
        // System.out.println("otpCode: "+otp);

        // 2. Format the Redis value: OTPCode,ExpirationTime
        // ExpirationTime is truncated to seconds.
        String redisValue = otp + "," + sessionDatetime.toString();
        // System.out.println("redisValue: "+redisValue);
        String redisKey = KEY_PREFIX + subjectId;

        // 3. Save to Redis with the expiry duration
        redisTemplate.opsForValue().set(redisKey, redisValue, OTP_EXPIRY_DURATION.toSeconds(), TimeUnit.SECONDS);
        // System.out.println("redis set ended");
        // // 4. Pre-populate the Attendance table
        // AttendanceService attendanceService = new AttendanceService();
        // attendanceService.prePopulateAttendance(subjectId, expirationTime);
        
        // 5. Return the full structured value needed by the controller
        return redisValue;
    }

    // /**
    //  * Generates and saves a one-time password for a specific subject.
    //  * @param subjectId The ID of the subject (e.g., "01")
    //  * @param otp       The 4-digit password (e.g., "1234")
    //  */
    // public void saveOtp(String subjectId, String otp, LocalDateTime sessionDatetime) {
    //     String key = KEY_PREFIX + subjectId;
        
    //     // Store both the OTP and the session time as a concatenated string or JSON in Redis
    //     String valueToStore = otp + "," + sessionDatetime.toString(); 

    //     // Use your existing Redis logic
    //     redisTemplate.opsForValue().set(key, valueToStore, 5, TimeUnit.MINUTES);
    // }

        // // This is the magic command:
        // // SET "otp:subject:01" "1234" (and expire in 5 minutes)
        // redisTemplate.opsForValue().set(key, otp, OTP_EXPIRY);
    // }

    /**
     * Validates an OTP submitted by a student.
     * (You will use this later for your Flutter app)
     *
     * @param subjectId The ID of the subject
     * @param otp       The OTP submitted by the student
     * @return true if the OTP is correct, false otherwise
     */
    public String validateOtp(String subjectId, String otp) {
        // Change the return type from boolean to String (or a custom DTO)
        // We'll return the full stored value (e.g., "1234:2025-11-09T10:00:00.000")
        String key = KEY_PREFIX + subjectId;
        
        // 1. Get the saved OTP from Redis
        String savedOtp = redisTemplate.opsForValue().get(key);
        System.out.println(savedOtp);
        
        // 2. Check for null/expiry first. If the key is expired or never existed, savedOtp is null.
        if (savedOtp == null) {
            // Log this! This should be the path for an invalid or expired OTP.
            System.out.println("Validation FAILED: OTP key not found or expired for subjectId: " + subjectId);
            return null; // MUST return false if the key doesn't exist
        }

        // 2. Parse the stored value (OTP:SESSION_DATETIME)
        String[] parts = savedOtp.split(",");
        if (parts.length != 2) {
            // If the stored value format is wrong, treat as failure
            return null; 
        }

        String storedOtp = parts[0];
        // String storedSessionDatetimeString = parts[1]; // We'll return the whole thing for now

        // 3. Compare the submitted OTP with the saved one
        // Use String.equals(String) for comparison
        if(otp.equals(storedOtp)){  // This will only be true if they match
            return savedOtp;
        } else{
            return null;
        }
    }

    public void deleteOtp(String subjectId) {
        String key = KEY_PREFIX + subjectId;
        redisTemplate.delete(key); // Deletes the key immediately
    }
}