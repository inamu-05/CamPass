package com.example.app.dto;

import java.util.List;
import lombok.Data;

@Data
public class CertificateHistoryResponseDto {
    private String studentId;
    private String studentName;
    private List<CertificateHistoryDto> history;

    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
}
