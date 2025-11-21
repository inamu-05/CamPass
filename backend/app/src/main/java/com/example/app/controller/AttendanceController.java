// OTP 用のコントローラー
package com.example.app.controller;

// import com.example.app.entity.Attendance;
import com.example.app.entity.Subject;
import com.example.app.service.SubjectService;
import com.example.app.service.AttendanceService;
import com.example.app.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Controller
public class AttendanceController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private OtpService otpService; // ⬅️ Inject OtpService

    @Autowired
    private AttendanceService attendanceService;


    // DBから選択肢のsubjectのリストを受け取り、htmlへ送る
    @GetMapping("/attendance/otp/create")
    public String showOtpCreatePage(Model model) {
        List<Subject> subjects = subjectService.findAllSubjects();
        model.addAttribute("subjects", subjects);
        return "main/onetimepass"; 
    }

    // redisを使って、OTPを保存する
    @PostMapping("/save-onetime-pass")
    @ResponseBody
    public ResponseEntity<?> saveOneTimePass(@RequestBody OtpRequest request) {
        try {
            // 1. Define the unique session identifier NOW
            ZoneId japanZoneId = ZoneId.of("Asia/Tokyo");
            LocalDateTime sessionDatetime = LocalDateTime.now(japanZoneId).truncatedTo(ChronoUnit.SECONDS);
            // System.out.println(japanZoneId);
            final Duration OTP_EXPIRY_DURATION = Duration.ofMinutes(5);

            // 2. Pre-Populate Attendance Records (The "Create Class" action)
            // This inserts an 'Absent' record for every student in the class.
            // System.out.println(request.getSubjectId());
            // System.out.println(sessionDatetime);
            attendanceService.prePopulateAttendance(request.getSubjectId(), sessionDatetime);
            // System.out.println("prePopulate Ended");

            // 3. Save the OTP to Redis, storing the sessionDatetime with it (Optional, but useful)
            // NOTE: You'll need to update your OtpService.saveOtp to accept sessionDatetime
            // OtpServiceを使って Redisに保存
            String response = otpService.saveOtp(request.getSubjectId(), request.getPass(), sessionDatetime); 
            
            // System.out.println("Save response: "+response);

            String otpCode = response.split(",")[0];
            // System.out.println("otpCode: "+otpCode);
            // String sessionStartTimeString = response.split(",")[1];
            // System.out.println("sessionStartTimeString: "+sessionStartTimeString);

            LocalDateTime expirationTime = sessionDatetime.plus(OTP_EXPIRY_DURATION).truncatedTo(ChronoUnit.SECONDS);

            OtpResponse sendResponse = new OtpResponse(otpCode, expirationTime);
            // System.out.println(sendResponse.toString());
            return ResponseEntity.ok(sendResponse);

        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error during OTP save and pre-population: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error creating OTP or class session.");

            // // エラー
            // System.err.println("Error saving OTP to Redis: " + e.getMessage());
            // return ResponseEntity.badRequest().build(); // HTTP 400
        }
    }


    @PostMapping("/api/validate-otp")
    @ResponseBody
    public ResponseEntity<String> validateOtp(@RequestBody AttendanceRequest request) {
        // 1. 受け取ったOTPと RedisのOTPを比べる
        String isValid = otpService.validateOtp(request.getSubjectId(), request.getPass());
        
        if (isValid != null) {
            try {
                // 2. Extract the Session Datetime from the stored data
                String[] parts = isValid.split(",");
                
                // The session datetime string should be the second part (index 1)
                String sessionDatetimeString = parts[1]; 
                // System.out.println(parts[0]+parts[1]);
                
                // Convert the string back to LocalDateTime
                LocalDateTime sessionDatetime = LocalDateTime.parse(sessionDatetimeString); 
                // sessionDatetime = sessionDatetime.truncatedTo(ChronoUnit.SECONDS);

                // System.out.println("sessionDatetime: "+sessionDatetime);

                // 3. Record Attendance using the CORRECT sessionDatetime
                attendanceService.recordAttendance(request, sessionDatetime);
                
                return ResponseEntity.ok("Attendance Recorded Successfully.");
                
            } catch (Exception e) {
                System.err.println("Error parsing datetime or recording attendance: " + e.getMessage());
                // If anything goes wrong AFTER validation, report an internal error.
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error during attendance recording.");
            }
        } else {
            // 4. Validation failed (pass wrong, expired, or already used)
            return ResponseEntity.status(401).body("Invalid or Expired Pass.");
        }
    }

    @GetMapping("/attendance/otp/display")
    public String showOtpDisplayPage() {
        // This simply returns the display.html template
        return "main/display"; 
    }
}