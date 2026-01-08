package com.example.app.dto;

import java.time.LocalDateTime;

public class AttendanceHistoryDto {
    private String name;        // Subject Name (授業名)
    private String date;        // Session Date (日時, formatted)
    private String teacher;     // Teacher Name (教員名)
    private String remarks;     // Attendance Status (備考)

    // Constructor
    public AttendanceHistoryDto(String name, LocalDateTime sessionDatetime, String teacher, String remarkCode) {
        this.name = name;
        
        // Format the date as 'YYYY/MM/DD' to match Flutter's expected sorting
        this.date = sessionDatetime.toLocalDate().toString().replace('-', '/');
        
        this.teacher = teacher;
        this.remarks = convertRemarkCode(remarkCode);
    }
    
    // Convert the single character remark code (A, L, etc.) to the displayed string
    private String convertRemarkCode(String code) {
        if (code == null) return "";
        switch (code) {
            case "0": return "出席";
            case "1": return "遅刻";
            case "2": return "欠席";
            default: return "";
        }
    }

    // Getters and Setters (omitted for brevity)
    // You must include standard getters/setters for Jackson serialization
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}