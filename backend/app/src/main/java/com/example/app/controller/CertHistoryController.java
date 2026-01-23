package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

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

    @GetMapping("/api/cert/history/page")
    public ResponseEntity<CertificateHistoryResponseDto> getMyHistoryWithPaging(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // JWT から studentId を取得
        String studentId = authentication.getName();

        Student student = studentRepository.findById(studentId)
            .orElseThrow();

        // ページ付き履歴取得
        Page<CertificateHistoryDto> historyPage =
                certManageService.getHistoryByStudentId(studentId, page, size);

        CertificateHistoryResponseDto response =
                new CertificateHistoryResponseDto();

        response.setStudentId(student.getUserId());
        response.setStudentName(student.getUserName());
        response.setHistory(historyPage.getContent());

        // ▼ ページ情報（Flutter用）
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(historyPage.getTotalPages());
        response.setTotalElements(historyPage.getTotalElements());
        response.setHasNext(historyPage.hasNext());
        response.setHasPrevious(historyPage.hasPrevious());

        return ResponseEntity.ok(response);
    }
}
