package com.example.app.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class AttendanceId implements Serializable{

    // 科目番号
    @Column(name = "subject_id", length = 2)
    private String subjectId;

    // 学生番号
    @Column(name = "user_id", length = 7)
    private String userId;

    // 空のコンストラクタ
    public AttendanceId() {}

    // ゲッター・セッター
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceId)) return false;
        AttendanceId that = (AttendanceId) o;
        return Objects.equals(subjectId, that.subjectId) &&
               Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, userId);
    }
}
