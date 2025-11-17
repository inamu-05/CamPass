package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.entity.Certificate;
import com.example.app.service.CertificateService;

@RestController
@CrossOrigin(origins = "*") // Flutter からのアクセスを許可
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    // 全証明書取得API
    @GetMapping("/api/certificates")
    public List<Certificate> getAllCertificates() {
        return certificateService.getAllCertificates();
    }
}
