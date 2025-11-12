package com.example.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class AttendanceId implements Serializable{

    // 論理名称:科目番号
    @Column(name = "subject_id", length = 2)
    private String subjectId;

    // 論理名称:学生番号
    @Column(name = "user_id")
    private String userId;

    // 論理名称:セッション日時
    @Column(name = "session_datetime")
	private LocalDateTime sessionDatetime;

    // 空のコンストラクタ
    public AttendanceId() {}

    public AttendanceId(String subjectId, String userId, LocalDateTime sessionDatetime) {
        this.subjectId = subjectId;
        this.userId = userId;
        this.sessionDatetime = sessionDatetime;
    }

    // ゲッター・セッター
    public void setId(AttendanceId id) {
        this.subjectId = id.getSubjectId();
        this.userId = id.getUserId();
        this.sessionDatetime = id.getSessionDatetime();
    }
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
    public LocalDateTime getSessionDatetime() {
        return sessionDatetime;
    }
    public void setSessionDatetime(LocalDateTime sessionDatetime) {
        this.sessionDatetime = sessionDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceId)) return false;
        AttendanceId that = (AttendanceId) o;
        return Objects.equals(subjectId, that.subjectId) &&
               Objects.equals(userId, that.userId) &&
               Objects.equals(sessionDatetime, that.sessionDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, userId, sessionDatetime);
    }
}
