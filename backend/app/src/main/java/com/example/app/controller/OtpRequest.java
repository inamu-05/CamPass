// File: src/main/java/com/example/app/controller/OtpRequest.java
// (You can put this in a 'dto' sub-package or inside the controller file)
package com.example.app.controller;

// This class will map to the JSON: { "subjectId": "...", "pass": "..." }
public class OtpRequest {
    private String subjectId;
    private String pass;

    // Getters and Setters
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
}