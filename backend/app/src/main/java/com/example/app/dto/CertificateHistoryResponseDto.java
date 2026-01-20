package com.example.app.dto;

import java.util.List;
import lombok.Data;

@Data
public class CertificateHistoryResponseDto {
    private String studentId;
    private String studentName;
    private List<CertificateHistoryDto> history;
}
