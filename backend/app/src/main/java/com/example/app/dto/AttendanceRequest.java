package com.example.app.dto;
// dto です

public class AttendanceRequest {
    private String subjectId;
    private String pass;
    private String userId;
    private String classId;

    // ゲッター、セッター
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getClassId() {
        return classId;
    }
    public void setclassId(String classId) {
        this.classId = classId;
    }
    
}
