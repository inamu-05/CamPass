package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.CertificateHistoryDto;
import com.example.app.service.CertManageService;
import com.example.app.dto.CertificateHistoryResponseDto;
import com.example.app.entity.Student;
import com.example.app.repository.StudentRepository;

@RestController
@CrossOrigin(origins = "*")
public class CertHistoryController {

    @Autowired
    private CertManageService certManageService;

    @Autowired
    private StudentRepository studentRepository;

    // ▼ 学生：証明書申請履歴一覧
    @GetMapping("/api/cert/history")
    public ResponseEntity<CertificateHistoryResponseDto> getMyHistory(
            Authentication authentication) {

        // JWT から studentId を取得
        String studentId = authentication.getName();

        Student student = studentRepository.findById(studentId)
            .orElseThrow();

        // 履歴取得
        List<CertificateHistoryDto> history =
                certManageService.getHistoryByStudentId(studentId);

        CertificateHistoryResponseDto response = new CertificateHistoryResponseDto();
        response.setStudentId(student.getUserId());
        response.setStudentName(student.getUserName());
        response.setHistory(history);

        return ResponseEntity.ok(response);
    }
}
