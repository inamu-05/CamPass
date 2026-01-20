package com.example.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CertificateHistoryDto {

    // 学生情報
    private String studentId;
    private String studentName;

    // 申請情報
    private String purchaseDate;
    private String name;        // 証明書名
    private Integer price;      // 単価
    private Integer quantity;
    private String payment;
    private String receive;
    private String status;
    private Integer situationOrder; // 並び替え専用
}
