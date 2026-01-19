package com.example.app.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cert_manage")
public class CertManage {

    // 主キー：申請ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Integer applicationId;

    // 証明書（FK）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;

    // 学生（FK）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Student student;

    // 部数
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // 受取方法
    @Column(name = "receive", length = 1, nullable = false)
    private String receive;

    // 支払方法
    @Column(name = "payment", length = 1, nullable = false)
    private String payment;

    // 申請日
    @Column(name = "requested_on", nullable = false)
    private LocalDate requestedOn;

    // 発行フラグ
    @Column(name = "is_printed", nullable = false)
    private Boolean isPrinted = false;

    // 状況
    @Column(name = "situation", length = 1, nullable = false)
    private String situation;

    // ===== コンストラクタ =====
    public CertManage() {}

    public CertManage(Certificate certificate, Student student, Integer quantity, String receive, String payment, LocalDate requestedOn, Boolean isPrinted, String situation) {
        this.certificate = certificate;
        this.student = student;
        this.quantity = quantity;
        this.receive = receive;
        this.payment = payment;
        this.requestedOn = requestedOn;
        this.isPrinted = isPrinted;
        this.situation = situation;
    }

    // ===== ゲッター・セッター =====

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public LocalDate getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(LocalDate requestedOn) {
        this.requestedOn = requestedOn;
    }

    public Boolean getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(Boolean isPrinted) {
        this.isPrinted = isPrinted;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}
