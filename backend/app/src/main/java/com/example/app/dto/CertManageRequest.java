package com.example.app.dto;

import lombok.Data;

@Data
public class CertManageRequest {
    private String studentId;
    private String certificateId;
    private Integer quantity;
    private String receive;
    private String payment;
}
