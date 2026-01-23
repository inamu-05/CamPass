package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.CertManageRequest;
import com.example.app.entity.CertManage;
import com.example.app.service.CertManageService;

@RestController
@CrossOrigin(origins = "*")  // Flutterからのアクセスを許可
public class CertApplicationController {

    @Autowired
    private CertManageService certManageService;

    // ▼ 学生の申請履歴一覧
    // @GetMapping("/api/cert/manage/{studentId}")
    // public List<CertManage> getListByStudent(@PathVariable String studentId) {
    //     return certManageService.findByStudentId(studentId);
    // }

    // ▼ 新規申請登録
    // @PostMapping("/api/cert/manage")
    // public CertManage applyCertificate(@RequestBody CertManage certManage) {
    //     return certManageService.save(certManage);
    // }

    @PostMapping("/api/certmanage")
    public ResponseEntity<?> createApplication(
            @RequestBody CertManageRequest request
    ) {
        // ★ JWT から studentId を取得
        String studentId = org.springframework.security.core.context
                .SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        CertManage saved = certManageService.createApplication(studentId, request);
        return ResponseEntity.ok(saved);
    }

}
