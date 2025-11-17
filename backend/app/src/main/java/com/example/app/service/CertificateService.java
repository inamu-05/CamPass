package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.entity.Certificate;
import com.example.app.repository.CertificateRepository;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    // 全証明書を取得
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }
}
