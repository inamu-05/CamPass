// File: com.example.app.controller.AttendanceResponseDto.java

package com.example.app.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime; // Use java.time for modern date/time

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDto {
    private String message; // The result message
    private String subjectName; // The name of the subject
    private LocalDateTime attendanceTime; // The actual time attendance was recorded
    // You might also include the subjectId and userId if needed
}