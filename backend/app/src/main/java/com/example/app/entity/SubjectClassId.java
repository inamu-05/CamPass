package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects; // For easy hashCode/equals implementation

@Embeddable
public class SubjectClassId implements Serializable {

    // Must match the column names in the SubjectClass table/entity
    
    // 論理名称: 科目ID (Foreign Key part 1)
    @Column(name = "subject_id", length = 2, nullable = false)
    private String subjectId;

    // 論理名称: ユーザーID (Foreign Key part 2)
    @Column(name = "user_id", length = 7, nullable = false)
    private String userId;

    // Default constructor is required by JPA
    public SubjectClassId() {}

    // Full constructor for convenience
    public SubjectClassId(String subjectId, String userId) {
        this.subjectId = subjectId;
        this.userId = userId;
    }

    // --- Getters/Setters (Required) ---
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // --- equals() and hashCode() (Crucial for composite keys) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectClassId that = (SubjectClassId) o;
        return Objects.equals(subjectId, that.subjectId) &&
               Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, userId);
    }
}