package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.entity.Certificate;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, String> {
    // List<Certificate> findByCertificateName(String name);
}